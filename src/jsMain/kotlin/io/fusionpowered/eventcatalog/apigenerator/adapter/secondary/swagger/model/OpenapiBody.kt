package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model

import io.fusionpowered.eventcatalog.common.stringMapOf

@JsExport
data class OpenapiBody(
  val description: String?
)

val OpenapiBody.mediaTypeMap: Map<String, String>
  get() =
    stringMapOf<String>(asDynamic().content).values.firstOrNull()
      ?.let { body -> stringMapOf(body) }
      ?: emptyMap()