package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.mapper

import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model.OpenapiParameter
import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiMessageData.Request.Parameter

fun OpenapiParameter.toParameter() =
  Parameter(
    name = name,
    location = `in`,
    required = required ?: false,
    description = description ?: "",
  )