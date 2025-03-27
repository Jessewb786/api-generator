package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.mapper

import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model.OpenapiDocument
import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiServiceData
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.jsmodule.Path

fun OpenapiDocument.toOpenapiServiceData(path: String) =
  OpenapiServiceData(
    name = info.title,
    version = info.version,
    schemaPath = Path.basename(path),
    description = info.description ?: "",
    tags = tags?.map { it.name } ?: emptyList(),
    externalDocumentation = externalDocs?.toExternalDocumentation()
  )