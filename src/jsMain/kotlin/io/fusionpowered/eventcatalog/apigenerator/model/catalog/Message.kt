package io.fusionpowered.eventcatalog.apigenerator.model.catalog

import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiMessageData

data class Message(
  val id: String,
  val name: String,
  val version: String,
  val summary: String,
  val schemaPath: String,
  val sidebar: Sidebar,
  val badges: Set<Badge>,
  val owners: Set<String> = emptySet(),
  val markdown: String,
) {

  constructor(openapiMessageData: OpenapiMessageData) : this(
    id = openapiMessageData.id,
    name = openapiMessageData.name,
    version = openapiMessageData.version,
    summary = openapiMessageData.run { summary.ifBlank { description.truncate(150) } },
    schemaPath = openapiMessageData.request.body?.let { "request-body.json" } ?: "",
    sidebar = Sidebar(badge = openapiMessageData.method.uppercase()),
    badges = setOf(openapiMessageData.method.uppercase().toBadge())
      .plus(openapiMessageData.tags.map { "tag:$it".toBadge() }),
    markdown = openapiMessageData.defaultMarkdown
  )

  companion object {

    private fun String.truncate(length: Int) =
      if (length >= this.length) this else substring(0, length - 3) + "..."

    private fun String.toBadge() =
      Badge(
        content = this,
        textColor = "blue",
        backgroundColor = "blue"
      )

    private val OpenapiMessageData.defaultMarkdown: String
      get() {
        val overviewMarkdown = when {
          description.isNotBlank() -> "## Overview\n$description\n\n"
          else -> ""
        }
        val externalDocumentationMarkdown = externalDocumentation
          ?.run { "## External documentation\n-[${description}](${url})\n" }
          ?: ""
        val requestBodyMarkdown = request.body?.content
          ?.run { "### Body\n<SchemaViewer file=\"request-body.json\" maxHeight=\"500\" id=\"request-body\" />\n\n" }
          ?: ""
        return overviewMarkdown +
          externalDocumentationMarkdown +
          "## ${method.uppercase()}(${path})\n\n" +
          request.parameters.defaultMarkdown +
          requestBodyMarkdown +
          responses.defaultMarkdown +
          "## Architecture\n\n<NodeGraph />\n"
      }

    private val List<OpenapiMessageData.Request.Parameter>.defaultMarkdown: String
      get() {
        val headerMarkdown = "### Parameters\n"

        val parametersMarkdown = map { parameter ->
          "- **${parameter.name}** (${parameter.location})${if (parameter.required) " (required)" else ""}: ${parameter.description}\n"
        }

        return when {
          parametersMarkdown.isNotEmpty() -> headerMarkdown + parametersMarkdown.filter { it.isNotBlank() }.joinToString("\n") + "\n"
          else -> ""
        }
      }

    private val Map<String, OpenapiMessageData.Response>.defaultMarkdown: String
      get() {
        val headerMarkdown = "### Responses\n\n"

        val responsesMarkdown = entries.map { (statusCode, response) ->
          val statusColor = when (statusCode[0]) {
            '2' -> "green"
            '4' -> "orange"
            '5' -> "red"
            else -> "gray"
          }

          val statusText = when (statusCode) {
            "200" -> " OK"
            "201" -> " Created"
            "202" -> " Accepted"
            "204" -> " No Content"
            "301" -> " Moved Permanently"
            "302" -> " Found"
            "304" -> " Not Modified"
            "400" -> " Bad Request"
            "401" -> " Unauthorized"
            "403" -> " Forbidden"
            "404" -> " Not Found"
            "405" -> " Method Not Allowed"
            "409" -> " Conflict"
            "422" -> " Unprocessable Entity"
            "429" -> " Too Many Requests"
            "500" -> " Internal Server Error"
            "502" -> " Bad Gateway"
            "503" -> " Service Unavailable"
            "504" -> " Gateway Timeout"
            else -> ""
          }

          val statusMarkdown = "#### <span className=\"text-${statusColor}-500\">${statusCode}${statusText}</span>\n"
          val bodyMarkdown = response.body.content
            ?.run { "<SchemaViewer file=\"response-${statusCode}.json\" maxHeight=\"500\" id=\"response-${statusCode}\" />\n" }
            ?: ""
          statusMarkdown + bodyMarkdown
        }

        return when {
          responsesMarkdown.isNotEmpty() -> headerMarkdown + responsesMarkdown.filter { it.isNotBlank() }.joinToString("\n") + "\n"
          else -> ""
        }
      }
  }

}


