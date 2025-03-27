package io.fusionpowered.eventcatalog.apigenerator.adapter.primary.plugin.model

@JsExport
data class Service(
  val id: String,
  val path: String,
  val owners: Array<String>? = null,
)