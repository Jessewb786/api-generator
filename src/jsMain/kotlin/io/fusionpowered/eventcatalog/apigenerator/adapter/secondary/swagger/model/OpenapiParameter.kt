package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model

@JsExport
data class OpenapiParameter(
  val name: String,
  val `in`: String,
  val required: Boolean?,
  val description: String?,
)