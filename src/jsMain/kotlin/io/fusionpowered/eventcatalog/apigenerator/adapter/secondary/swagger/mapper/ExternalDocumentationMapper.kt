package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.mapper

import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model.OpenapiExternalDocumentation
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.ExternalDocumentation

fun OpenapiExternalDocumentation.toExternalDocumentation() =
  ExternalDocumentation(
    description = description ?: "",
    url = url ?: ""
  )