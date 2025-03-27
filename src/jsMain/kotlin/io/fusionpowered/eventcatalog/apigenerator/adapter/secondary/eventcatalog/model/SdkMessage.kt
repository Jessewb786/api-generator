package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.eventcatalog.model

@JsExport
data class SdkMessage(
  val id: String,
  val name: String,
  val summary: String,
  val version: String,
  val badges: Array<SdkBadge>?,
  val sidebar: SdkSidebar?,
  val owners: Array<String>?,
  val schemaPath: String,
  val markdown: String
)
