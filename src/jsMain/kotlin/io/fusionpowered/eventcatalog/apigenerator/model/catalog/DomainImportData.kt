package io.fusionpowered.eventcatalog.apigenerator.model.catalog

data class DomainImportData(
  val id: String,
  val name: String,
  val version: String,
  val owners: Set<String>,
)