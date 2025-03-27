package io.fusionpowered.eventcatalog.apigenerator.model.catalog

import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiServiceData

data class Service(
  val id: String,
  val name: String,
  val version: String,
  val summary: String,
  val schemaPath: String,
  val badges: Set<Badge>,
  val owners: Set<String>,
  val specifications: Specifications,
  val repository: Repository = Repository(),
  val sends: MutableList<ResourcePointer> = mutableListOf(),
  val receives: MutableList<ResourcePointer> = mutableListOf(),
  val markdown: String,
) {

  constructor(importData: ServiceImportData, openapiData: OpenapiServiceData) : this(
    id = importData.id,
    name = openapiData.name,
    summary = openapiData.description.truncate(150),
    version = openapiData.version,
    badges = openapiData.tags.map { it.toBadge() }.toSet(),
    owners = importData.owners,
    schemaPath = openapiData.schemaPath,
    markdown = openapiData.defaultMarkdown,
    specifications = Specifications(
      openapiPath = openapiData.schemaPath,
    ),
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

    private val OpenapiServiceData.defaultMarkdown: String
      get() {
        val titleMarkdown = description.let {
          if (it.isNotBlank()) {
            """
                $it
                
              """.trimIndent()
          } else {
            ""
          }
        }

        val diagramMarkdown =
          """
            ## Architecture diagram
            <NodeGraph />
          """.trimIndent()

        val externalDocumentationMarkdown = externalDocumentation
          ?.let {
            """
        
              ## External documentation
              -[${it.description}](${it.url})
            """.trimIndent()
          }
          ?: ""

        return titleMarkdown + diagramMarkdown + externalDocumentationMarkdown
      }

  }

}