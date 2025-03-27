package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.mapper

import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.jsmodule.slugify
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model.OpenapiDocument
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model.OpenapiOperation
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model.extensions
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model.responses
import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiMessageData
import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiMessageData.Direction
import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiMessageData.Type

fun OpenapiOperation.toOpenapiMessageData(path: String, document: OpenapiDocument, method: String): OpenapiMessageData {
  val alternativeId = operationId ?: "${slugify(document.info.title)}_${method.uppercase()}_${path.replace("/", "")}"
    .trim('_')
  return OpenapiMessageData(
    id = extensions["x-eventcatalog-message-id"] ?: alternativeId,
    name = extensions["x-eventcatalog-message-name"] ?: alternativeId,
    version = extensions["x-eventcatalog-message-version"] ?: document.info.version,
    path = path,
    method = method.uppercase(),
    summary = summary ?: "",
    description = description ?: "",
    type = extensions["x-eventcatalog-message-type"]?.toType() ?: Type.Query,
    direction = when {
      extensions["x-eventcatalog-message-action"] === "sends" -> Direction.Sends
      else -> Direction.Receives
    },
    externalDocumentation = externalDocs?.toExternalDocumentation(),
    tags = tags?.toList() ?: emptyList(),
    request = OpenapiMessageData.Request(
      parameters = parameters?.map { it.toParameter() } ?: emptyList(),
      body = request?.toBody(),
    ),
    responses = responses.mapValues { it.toResponse() }
  )
}
