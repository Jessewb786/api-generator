package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.mapper

import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiMessageData.Type

fun String.toType() =
  Type.valueOf(replaceFirstChar(Char::uppercase))