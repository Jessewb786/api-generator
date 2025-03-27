package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.mapper

import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model.OpenapiBody
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model.mediaTypeMap
import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiMessageData
import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiMessageData.Content
import io.fusionpowered.eventcatalog.common.circularStringify

fun OpenapiBody.toBody() =
  OpenapiMessageData.Body(
    description = description ?: "",
    content = when {
      mediaTypeMap.isEmpty() -> null
      mediaTypeMap.containsKey("schema") -> Content.Schema(circularStringify(mediaTypeMap["schema"]))
      else -> Content.Other(circularStringify(this))
    }
  )
