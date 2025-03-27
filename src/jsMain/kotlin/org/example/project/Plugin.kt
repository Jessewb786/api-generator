@file:OptIn(ExperimentalJsExport::class)
package org.example.project

@JsModule("chalk")
@JsNonModule
external class Chalk {

  companion object {
    fun green(str: String): String
  }

}

external val process: dynamic

@OptIn(ExperimentalJsExport::class)
@JsExport
fun generator(pluginConfig: dynamic) {
  console.log(pluginConfig.toString())
  console.log(Chalk.green("Hello, Kotlin/JS!"))
}