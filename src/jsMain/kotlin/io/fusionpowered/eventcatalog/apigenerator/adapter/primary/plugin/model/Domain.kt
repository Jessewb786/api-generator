package io.fusionpowered.eventcatalog.apigenerator.adapter.primary.plugin.model

@JsExport
data class Domain(
  val id: String,
  val name: String,
  val version: String,
  val owners: Array<String>? = null,
)