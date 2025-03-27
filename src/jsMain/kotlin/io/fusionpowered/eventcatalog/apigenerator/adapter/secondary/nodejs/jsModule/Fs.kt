package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.nodejs.jsModule

@JsModule("node:fs")
@JsNonModule
external class Fs {

  companion object {

    fun readFileSync(path: String, encoding: String): String?

  }

}