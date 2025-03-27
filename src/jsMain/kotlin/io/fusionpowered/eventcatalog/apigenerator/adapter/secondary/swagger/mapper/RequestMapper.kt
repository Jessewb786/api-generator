package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.mapper

import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model.OpenapiOperation
import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiMessageData

fun OpenapiOperation.toRequest() =
  OpenapiMessageData.Request(
    parameters = parameters?.map { it.toParameter() } ?: emptyList(),
    body = request?.toBody()
  )