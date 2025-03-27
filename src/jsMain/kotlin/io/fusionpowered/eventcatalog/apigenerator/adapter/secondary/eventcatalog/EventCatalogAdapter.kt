package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.eventcatalog

import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.eventcatalog.jsmodule.eventCatalogSdk
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.eventcatalog.mapper.*
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.eventcatalog.model.SdkFile
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.eventcatalog.model.SdkResourcePointer
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.eventcatalog.model.WriteOptions
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.Domain
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.Message
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.ResourcePointer
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.Service
import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiMessageData
import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiMessageData.Type.*
import io.fusionpowered.eventcatalog.apigenerator.port.EventCatalog
import kotlinx.coroutines.await

private external val process: dynamic

class EventCatalogAdapter : EventCatalog {

  private val sdk = eventCatalogSdk(process.env["PROJECT_DIR"].unsafeCast<String?>() ?: process.cwd().unsafeCast<String>())

  override suspend fun getLatestDomain(id: String): Domain? {
    return sdk.getDomain(id, "latest").await()?.toDomain()
  }

  override suspend fun versionDomain(id: String) {
    sdk.versionDomain(id).await()
  }

  override suspend fun writeDomain(domain: Domain) {
    sdk.writeDomain(domain.toSdkDomain(), WriteOptions()).await()
  }

  override suspend fun getLatestService(id: String): Service? {
    return sdk.getService(id, "latest").await()?.toService()
  }

  override suspend fun versionService(id: String) {
    sdk.versionService(id).await()
  }

  override suspend fun writeService(service: Service, domain: Domain?) {
    when {
      domain != null -> sdk.writeServiceToDomain(service.toSdkService(), SdkResourcePointer(domain.id, domain.version), WriteOptions()).await()
      else -> sdk.writeService(service.toSdkService(), WriteOptions()).await()
    }
    domain?.run {
      val indexOfService = services.indexOfFirst { it.id == service.id }
      when(indexOfService) {
        -1 -> services.add(ResourcePointer(service.id, service.version))
        else -> services[indexOfService] = ResourcePointer(service.id, service.version)
      }
      sdk.writeDomain(domain.toSdkDomain(), WriteOptions(override = true)).await()
    }
  }

  override suspend fun addFileToService(id: String, filename: String, content: String) {
    sdk.addFileToService(id, SdkFile(filename, content), "latest").await()
  }

  override suspend fun getLatestMessage(id: String): Message? {
    return sdk.getEvent(id, "latest").await()?.toMessage()
  }

  override suspend fun versionMessage(id: String, type: OpenapiMessageData.Type) {
    when (type) {
      Event ->  sdk.versionEvent(id).await()
      Command -> sdk.versionCommand(id).await()
      Query -> sdk.versionQuery(id).await()
    }
  }

  override suspend fun writeMessage(
    message: Message,
    service: Service,
    type: OpenapiMessageData.Type,
    direction: OpenapiMessageData.Direction
  ) {
    when (type) {
      Event -> sdk.writeEventToService(message.toSdkMessage(), SdkResourcePointer(service.id, "latest"), WriteOptions(override = true)).await()
      Command -> sdk.writeCommandToService(message.toSdkMessage(), SdkResourcePointer(service.id, "latest"), WriteOptions(override = true)).await()
      Query -> sdk.writeQueryToService(message.toSdkMessage(), SdkResourcePointer(service.id, "latest"), WriteOptions(override = true)).await()
    }
    when(direction) {
      OpenapiMessageData.Direction.Sends -> service.sends.add(ResourcePointer(message.id, message.version))
      OpenapiMessageData.Direction.Receives -> service.receives.add(ResourcePointer(message.id, message.version))
    }
    sdk.writeService(service.toSdkService(), WriteOptions(override = true)).await()
  }

  override suspend fun addFileToMessage(id: String, type: OpenapiMessageData.Type, filename: String, content: String) {
    when (type) {
      Event -> sdk.addFileToEvent(id, SdkFile(filename, content), "latest").await()
      Command -> sdk.addFileToCommand(id, SdkFile(filename, content), "latest").await()
      Query -> sdk.addFileToQuery(id, SdkFile(filename, content), "latest").await()
    }
  }

}