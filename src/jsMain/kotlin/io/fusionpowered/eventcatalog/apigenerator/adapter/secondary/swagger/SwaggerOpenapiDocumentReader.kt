package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger

import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.nodejs.NodejsLogger
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.jsmodule.SwaggerParser
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.mapper.toOpenapiMessageData
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.mapper.toOpenapiServiceData
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model.OpenapiDocument
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model.pathMap
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model.toMap
import io.fusionpowered.eventcatalog.apigenerator.port.Logger
import io.fusionpowered.eventcatalog.apigenerator.port.OpenapiDocumentReader
import kotlinx.coroutines.await

class SwaggerOpenapiDocumentReader private constructor(
  path: String,
  document: OpenapiDocument
) : OpenapiDocumentReader {

  class Factory(
    private val logger: Logger = NodejsLogger()
  ) : OpenapiDocumentReader.Factory {

    override suspend fun create(path: String): OpenapiDocumentReader {
      val document = try {
        SwaggerParser.validate(path).await()
        SwaggerParser.dereference(path).await()
      } catch (throwable: Throwable) {
        logger.error("Failed to parse OpenAPI file: $path")
        throw throwable
      }

      return SwaggerOpenapiDocumentReader(path, document)
    }

  }

  override val openapiServiceData = document.toOpenapiServiceData(path)

  override val openapiMessagesData =
    document.pathMap
      .flatMap { (path, openapiMethodToOperationMapReference) ->
        openapiMethodToOperationMapReference.toMap()
          .map { (method, operation) ->
            operation.toOpenapiMessageData(path, document, method)
          }
      }

}


