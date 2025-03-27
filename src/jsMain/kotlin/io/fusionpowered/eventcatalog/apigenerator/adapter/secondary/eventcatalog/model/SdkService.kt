package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.eventcatalog.model

@Suppress("NON_EXPORTABLE_TYPE")
@JsExport
data class SdkService(
  val id: String,
  val name: String,
  val version: String,
  val summary: String,
  val schemaPath: String,
  val badges: Array<SdkBadge>?,
  val owners: Array<String>?,
  val markdown: String,
  val repository: SdkRepository?,
  val sends: Array<SdkResourcePointer>?,
  val receives: Array<SdkResourcePointer>?,
  val specifications: dynamic,
)