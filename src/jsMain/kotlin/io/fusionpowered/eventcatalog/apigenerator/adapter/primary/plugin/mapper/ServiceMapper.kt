package io.fusionpowered.eventcatalog.apigenerator.adapter.primary.plugin.mapper

import io.fusionpowered.eventcatalog.apigenerator.adapter.primary.plugin.model.Service
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.ServiceImportData

fun Service.toServiceImportData() =
  ServiceImportData(
    id = id,
    openapiPath = path,
    owners = owners?.toSet() ?: emptySet()
  )