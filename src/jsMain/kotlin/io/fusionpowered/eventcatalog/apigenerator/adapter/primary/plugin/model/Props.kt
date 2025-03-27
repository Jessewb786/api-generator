package io.fusionpowered.eventcatalog.apigenerator.adapter.primary.plugin.model


@JsExport
data class Props(
  val services: Array<Service>,
  val domain: Domain? = null,
)