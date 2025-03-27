package io.fusionpowered.eventcatalog.apigenerator.port

import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiMessageData
import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiServiceData

interface OpenapiDocumentReader {

  interface Factory {
    suspend fun create(path: String): OpenapiDocumentReader
  }

  val openapiServiceData: OpenapiServiceData

  val openapiMessagesData: List<OpenapiMessageData>

}