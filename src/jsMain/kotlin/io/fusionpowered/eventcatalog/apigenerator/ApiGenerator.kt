package io.fusionpowered.eventcatalog.apigenerator

import io.fusionpowered.eventcatalog.apigenerator.model.catalog.DomainImportData
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.ServiceImportData


interface ApiGenerator {

  suspend fun generate(services: Set<ServiceImportData>, domainImportData: DomainImportData?)

}