package io.fusionpowered.eventcatalog.apigenerator.adapter.primary.plugin.mapper

import io.fusionpowered.eventcatalog.apigenerator.adapter.primary.plugin.model.Domain
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.DomainImportData

fun Domain.toDomainImportData() =
  DomainImportData(
    id = id,
    name = name,
    version = version,
    owners = owners?.toSet() ?: emptySet()
  )