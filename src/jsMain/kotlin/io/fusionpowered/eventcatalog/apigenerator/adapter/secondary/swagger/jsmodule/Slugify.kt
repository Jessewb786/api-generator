package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.swagger.jsmodule

@JsModule("slugify")
@JsNonModule
private external fun jsSlugify(word: String, options: SlugifyOptions): String

@JsExport
data class SlugifyOptions(
  val lower: Boolean
)

fun slugify(text: String) =
  jsSlugify(text.lowercase(), SlugifyOptions(lower = true))