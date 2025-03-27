package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model

import io.fusionpowered.eventcatalog.common.stringMapOf

@JsExport
interface OpenapiMethodToOperationMapReference

fun OpenapiMethodToOperationMapReference.toMap() =
  stringMapOf<OpenapiOperation>(this)