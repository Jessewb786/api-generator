package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.jsmodule

import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.model.OpenapiDocument
import kotlin.js.Promise

@JsModule("@apidevtools/swagger-parser")
@JsNonModule
external class SwaggerParser {

  companion object {

    fun validate(specPath: String): Promise<Unit>

    fun dereference(specPath: String): Promise<OpenapiDocument>

  }

}