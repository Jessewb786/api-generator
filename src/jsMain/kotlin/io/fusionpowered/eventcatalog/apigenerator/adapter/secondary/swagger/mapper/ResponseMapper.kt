package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.mapper

import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model.OpenapiBody
import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiMessageData

fun Map.Entry<String, OpenapiBody>.toResponse() =
  OpenapiMessageData.Response(
    status = key,
    body = value.toBody()
  )