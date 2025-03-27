package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.eventcatalog.mapper

import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.eventcatalog.model.SdkService
import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.eventcatalog.model.SdkSpecifications
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.Repository
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.Service
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.Specifications

fun SdkService.toService() =
  Service(
    id = id,
    name = name,
    version = version,
    summary = summary,
    schemaPath = schemaPath,
    badges = badges?.map { it.toBadge() }?.toSet() ?: emptySet(),
    owners = owners?.toSet() ?: emptySet(),
    markdown = markdown,
    repository = repository?.toRepository() ?: Repository(),
    sends = sends?.map { it.toResourcePointer() }?.toMutableList() ?: mutableListOf(),
    receives = receives?.map { it.toResourcePointer() }?.toMutableList() ?: mutableListOf(),
    specifications = specifications.unsafeCast<SdkSpecifications?>()?.toSpecifications() ?: Specifications(),
  )

fun Service.toSdkService() =
  SdkService(
    id = id,
    name = name,
    version = version,
    summary = summary,
    schemaPath = schemaPath,
    badges = badges.map { it.toSdkBadge() }.toTypedArray(),
    owners = owners.toTypedArray(),
    markdown = markdown,
    repository = repository.toSdkRepository(),
    sends = sends.map { it.toSdkResourcePointer() }.toTypedArray(),
    receives = receives.map { it.toSdkResourcePointer() }.toTypedArray(),
    specifications = specifications.toSdkSpecifications()
      /**
       * The sdk requires that unused specs not exist as a property in the object
       * So we need this hack to clear them
       */
      .also { removeEmptyProperties(it) }
  )

fun removeEmptyProperties(specs: SdkSpecifications) {
  js(
    """
       for (var key in specs) {
         if (specs[key].length === 0) {
             delete specs[key];
          }
       };
    """
  )
}