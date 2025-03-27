package io.fusionpowered.eventcatalog.apigenerator.model.openapi

import io.fusionpowered.eventcatalog.apigenerator.model.catalog.ExternalDocumentation


data class OpenapiMessageData(
  val id: String,
  val name: String,
  val version: String,
  val path: String,
  val method: String,
  val summary: String,
  val description: String,
  val type: Type,
  val direction: Direction,
  val externalDocumentation: ExternalDocumentation?,
  val tags: List<String>,
  val request: Request,
  val responses: Map<String, Response>
) {

  data class Request(
    val parameters: List<Parameter> = emptyList(),
    val body: Body? = null,
  ) {

    data class Parameter(
      val name: String,
      val location: String,
      val required: Boolean,
      val description: String,
    )

  }

  data class Response(
    val status: String,
    val body: Body
  )

  data class Body(
    val description: String,
    val content: Content? = null
  )

  sealed interface Content {

    val value: String

    class Schema(override val value: String) : Content

    class Other(override val value: String) : Content

  }

  enum class Type {
    Event,
    Command,
    Query
  }

  enum class Direction {
    Sends,
    Receives
  }

}


