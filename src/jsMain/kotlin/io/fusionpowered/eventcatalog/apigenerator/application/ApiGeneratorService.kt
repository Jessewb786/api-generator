package io.fusionpowered.eventcatalog.apigenerator.application

import io.fusionpowered.eventcatalog.apigenerator.ApiGenerator
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.eventcatalog.EventCatalogAdapter
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.nodefetch.NodeFetchFileDownloader
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.nodejs.NodejsFileSystem
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.nodejs.NodejsLogger
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.SwaggerOpenapiDocumentReader
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.Domain
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.DomainImportData
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.Message
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.Service
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.ServiceImportData
import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiMessageData
import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiServiceData
import io.fusionpowered.eventcatalog.apigenerator.port.*
import kotlin.js.Date


class ApiGeneratorService(
  private val logger: Logger = NodejsLogger(),
  private val openapiDocumentReaderFactory: OpenapiDocumentReader.Factory = SwaggerOpenapiDocumentReader.Factory(),
  private val catalog: EventCatalog = EventCatalogAdapter(),
  private val fileSystem: FileSystem = NodejsFileSystem(),
  private val fileDownloader: FileDownloader = NodeFetchFileDownloader()
) : ApiGenerator {

  override suspend fun generate(services: Set<ServiceImportData>, domainImportData: DomainImportData?) {
    val domain = generateDomain(domainImportData)

    services.forEach { serviceImportData ->
      val openapiReader = openapiDocumentReaderFactory.create(serviceImportData.openapiPath)

      val service = generateService(domain, serviceImportData, openapiReader.openapiServiceData)

      openapiReader.openapiMessagesData.forEach { openapiMessageData ->
        generateMessage(service, openapiMessageData)
      }
    }
  }

  private suspend fun generateDomain(domainImportData: DomainImportData?) =
    domainImportData
      ?.let(::Domain)
      ?.apply { logger.highlightedInfo("\nProcessing domain: $name (v$version)") }
      ?.let { domain ->
        val latestDomain = catalog.getLatestDomain(domain.id)
        when {
          latestDomain == null ->
            domain
              .apply {
                catalog.writeDomain(this)
                logger.info(" - Domain (v$version) created")
              }

          latestDomain.version != domain.version ->
            domain
              .copy(
                services = (domain.services + latestDomain.services).toMutableList(),
                owners = domain.owners + domain.owners,
                markdown = latestDomain.markdown
              )
              .apply {
                catalog.versionDomain(id)
                logger.warn(" - Versioned previous domain (v${latestDomain.version})")
                catalog.writeDomain(this)
                logger.info(" - Domain (v$version) created")
              }

          else ->
            domain
              .apply { logger.warn(" - Domain (v$version) already exists, skipped creation...") }
        }
      }

  private suspend fun generateService(
    domain: Domain?,
    serviceImportData: ServiceImportData,
    openapiServiceData: OpenapiServiceData,
  ) =
    Service(serviceImportData, openapiServiceData)
      .apply { logger.highlightedInfo("Processing service: $name (v$version)") }
      .let { service ->
        val latestService = catalog.getLatestService(service.id)
        when {
          latestService == null ->
            service
              .apply {
                catalog.writeService(this, domain)
                addAdditionalServiceFiles(this, serviceImportData)
                logger.info(" - Service (v$version) created")
              }

          latestService.version != service.version ->
            service
              .copy(
                badges = service.badges + latestService.badges,
                sends = (service.sends + latestService.sends).toMutableList(),
                owners = service.owners + latestService.owners,
                repository = latestService.repository,
                markdown = latestService.markdown,
              )
              .apply {
                catalog.versionService(id)
                logger.warn(" - Versioned previous service (v${latestService.version})")
                catalog.writeService(this, domain)
                addAdditionalServiceFiles(this, serviceImportData)
                logger.info(" - Service (v$version) created")
              }


          else ->
            service
              .copy(
                badges = service.badges + latestService.badges,
                sends = (service.sends + latestService.sends).toMutableList(),
                owners = service.owners + latestService.owners,
                repository = latestService.repository,
                markdown = latestService.markdown,
              )
              .apply { logger.warn(" - Service (v${service.version}) already exists, skipped creation...") }
        }
      }

  private suspend fun addAdditionalServiceFiles(
    service: Service,
    serviceImportData: ServiceImportData,
  ) {

    val content = when {
      serviceImportData.openapiPath.startsWith("http") -> fileDownloader.download(serviceImportData.openapiPath)
      else -> fileSystem.readFile(serviceImportData.openapiPath)
    }
    content?.let {
      catalog.addFileToService(
        id = service.id,
        filename = service.schemaPath,
        content = it
      )
    }

    catalog.addFileToService(
      id = service.id,
      filename = "changelog.mdx",
      content = "---\ncreatedAt: ${Date().toISOString().split('T')[0]}\n---\n"
    )
  }

  private suspend fun generateMessage(
    service: Service,
    openapiMessageData: OpenapiMessageData
  ) =
    Message(openapiMessageData)
      .apply { logger.highlightedInfo("Processing message: $name (v$version)") }
      .let { message -> message.copy(owners = message.owners + service.owners) }
      .let { message ->
        val latestMessage = catalog.getLatestMessage(message.id)
        when {
          latestMessage == null ->
            message
              .apply {
                catalog.writeMessage(this, service, openapiMessageData.type, openapiMessageData.direction)
                addAdditionalMessageFiles(this, openapiMessageData)
                logger.info(" - Message (v$version) created")
              }

          latestMessage.version != message.version ->
            message
              .copy(
                owners = message.owners + latestMessage.owners,
                badges = message.badges + latestMessage.badges,
                markdown = latestMessage.markdown
              )
              .apply {
                catalog.versionMessage(id, openapiMessageData.type)
                logger.info(" - Versioned previous message: (v$version)")
                catalog.writeMessage(this, service, openapiMessageData.type, openapiMessageData.direction)
                addAdditionalMessageFiles(this, openapiMessageData)
                logger.info(" - Message (v$version) created")
              }

          else ->
            message
              .copy(
                owners = message.owners + latestMessage.owners,
                badges = message.badges + latestMessage.badges,
                markdown = latestMessage.markdown
              )
              .apply {
                logger.warn(" - Message (v$version) already exists, overwriting previous message...")
                catalog.writeMessage(this, service, openapiMessageData.type, openapiMessageData.direction)
                addAdditionalMessageFiles(this, openapiMessageData)
                logger.info(" - Message (v$version) created")
              }
        }
      }


  private suspend fun addAdditionalMessageFiles(
    message: Message,
    openapiMessageData: OpenapiMessageData,
  ) {
    openapiMessageData.request.body?.content?.run {
      catalog.addFileToMessage(
        id = message.id,
        type = openapiMessageData.type,
        filename = "request-body.json",
        content = value
      )
    }

    openapiMessageData.responses.forEach { (statusCode, response) ->
      response.body.content?.run {
        catalog.addFileToMessage(
          id = message.id,
          type = openapiMessageData.type,
          filename = "response-$statusCode.json",
          content = value
        )
      }
    }

    catalog.addFileToMessage(
      id = message.id,
      type = openapiMessageData.type,
      filename = "changelog.mdx",
      content = "---\ncreatedAt: ${Date().toISOString().split('T')[0]}\n---\n"
    )
  }

}