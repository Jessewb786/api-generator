package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model

@JsExport
data class OpenapiInfo(
  val title: String,
  val description: String? = null,
  val termsOfService: String?,
  val contact: OpenapiContact?,
  val license: OpenapiLicense?,
  val version: String
)
