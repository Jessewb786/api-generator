package io.fusionpowered.eventcatalog.apigenerator.model.catalog

data class ServiceImportData(
  val id: String,
  val openapiPath: String,
  val owners: Set<String>,
)