package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model

import io.fusionpowered.eventcatalog.common.stringMapOf

@JsExport
external interface OpenapiOperation {

  val summary: String?
  val operationId: String?
  val description: String?
  val externalDocs: OpenapiExternalDocumentation?
  val tags: Array<String>?
  val parameters: Array<OpenapiParameter>?
  @JsName("requestBody")
  val request: OpenapiBody?

}

val OpenapiOperation.extensions: Map<String, String?>
  get() =
    stringMapOf<String>(this)
      .filter { (propertyName, _) -> propertyName.startsWith("x-eventcatalog-") }

val OpenapiOperation.responses: Map<String, OpenapiBody>
  get() =
    stringMapOf(asDynamic().responses)