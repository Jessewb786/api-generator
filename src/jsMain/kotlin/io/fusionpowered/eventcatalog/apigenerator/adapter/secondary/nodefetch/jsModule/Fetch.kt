package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.nodefetch.jsModule

import kotlin.js.Promise

@JsModule("node-fetch")
@JsNonModule
external fun fetch(url: String): Promise<Response>

@JsExport
external interface Response {

  fun text(): Promise<String?>

}