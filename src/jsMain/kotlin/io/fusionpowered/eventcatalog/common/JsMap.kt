package io.fusionpowered.eventcatalog.common


fun <T> stringMapOf(jsObject: dynamic): Map<String, T> = when {
  jsObject !== undefined -> entriesOf(jsObject.unsafeCast<T>()).toMap()
  else -> emptyMap()
}

private fun <T> entriesOf(jsObject: T): List<Pair<String, T>> =
  (js("Object.entries") as (dynamic) -> Array<Array<T>>)
    .invoke(jsObject)
    .map { entry -> entry[0] as String to entry[1] }
