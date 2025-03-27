package io.fusionpowered.eventcatalog.apigenerator.model.catalog

data class Domain(
  val id: String,
  val name: String,
  val version: String,
  val summary: String = "",
  val services: MutableList<ResourcePointer> = mutableListOf(),
  val owners: Set<String>,
  val markdown: String,
) {

  companion object {

    private const val DEFAULT_MARKDOWN = "## Architecture diagram\n<NodeGraph />\n\n"

  }

  internal constructor(importData: DomainImportData) : this(
    id = importData.id,
    name = importData.name,
    version = importData.version,
    markdown = DEFAULT_MARKDOWN,
    owners = importData.owners,
  )

}
