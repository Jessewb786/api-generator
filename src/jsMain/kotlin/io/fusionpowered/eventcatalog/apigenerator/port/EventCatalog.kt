package io.fusionpowered.eventcatalog.apigenerator.port

import io.fusionpowered.eventcatalog.apigenerator.model.catalog.Domain
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.Message
import io.fusionpowered.eventcatalog.apigenerator.model.catalog.Service
import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiMessageData.Direction
import io.fusionpowered.eventcatalog.apigenerator.model.openapi.OpenapiMessageData.Type


interface EventCatalog {

  suspend fun getLatestDomain(id: String): Domain?

  suspend fun versionDomain(id: String)

  suspend fun writeDomain(domain: Domain)

  suspend fun getLatestService(id: String): Service?

  suspend fun versionService(id: String)

  suspend fun writeService(service: Service, domain: Domain?)

  suspend fun addFileToService(id: String, filename: String, content: String)

  suspend fun getLatestMessage(id: String): Message?

  suspend fun versionMessage(id: String, type: Type)

  suspend fun writeMessage(message: Message, service: Service, type: Type, direction: Direction)

  suspend fun addFileToMessage(id: String, type: Type, filename: String, content: String)

}