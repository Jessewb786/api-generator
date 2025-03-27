package io.fusionpowered.eventcatalog.apigenerator.model.openapi

import io.fusionpowered.eventcatalog.apigenerator.model.catalog.ExternalDocumentation


data class OpenapiServiceData(
  val name: String,
  val version: String,
  val schemaPath: String,
  val description: String,
  val tags: List<String>,
  val externalDocumentation: ExternalDocumentation?
)