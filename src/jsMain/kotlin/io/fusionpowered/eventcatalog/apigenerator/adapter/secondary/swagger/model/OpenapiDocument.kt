package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model

import io.fusionpowered.eventcatalog.common.stringMapOf

@JsExport
data class OpenapiDocument(
  val tags: Array<OpenapiTag>?,
  val info: OpenapiInfo,
  val externalDocs: OpenapiExternalDocumentation?
)

val OpenapiDocument.pathMap: Map<String, OpenapiMethodToOperationMapReference>
  get() = stringMapOf(asDynamic().paths)