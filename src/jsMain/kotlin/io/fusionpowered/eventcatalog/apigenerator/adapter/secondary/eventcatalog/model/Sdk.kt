package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.eventcatalog.model

import kotlin.js.Promise


external interface Sdk {

  fun getDomain(id: String, version: String?): Promise<SdkDomain?>

  fun versionDomain(id: String): Promise<Unit>

  fun writeDomain(domain: SdkDomain, options: WriteOptions): Promise<Unit>

  fun addServiceToDomain(id: String, service: SdkResourcePointer, version: String?): Promise<Unit>

  fun getService(id: String, version: String?): Promise<SdkService?>

  fun versionService(id: String): Promise<Unit>

  fun writeService(service: SdkService, options: WriteOptions): Promise<Unit>

  fun writeServiceToDomain(service: SdkService, domain: SdkResourcePointer, options: WriteOptions): Promise<Unit>

  fun addFileToService(id: String, file: SdkFile, version: String?): Promise<Unit>

  fun getEvent(id: String, version: String?): Promise<SdkMessage?>

  fun versionEvent(id: String): Promise<Unit>

  fun writeEvent(event: SdkMessage, options: WriteOptions): Promise<Unit>

  fun writeEventToService(event: SdkMessage, service: SdkResourcePointer, options: WriteOptions): Promise<Unit>

  fun addFileToEvent(id: String, file: SdkFile, version: String?): Promise<Unit>

  fun addEventToService(id: String, direction: String, event: SdkResourcePointer, version: String?): Promise<Unit>

  fun getCommand(id: String, version: String?): Promise<SdkMessage?>

  fun versionCommand(id: String): Promise<Unit>

  fun writeCommand(command: SdkMessage, options: WriteOptions): Promise<Unit>

  fun writeCommandToService(command: SdkMessage, service: SdkResourcePointer, options: WriteOptions): Promise<Unit>

  fun addFileToCommand(id: String, file: SdkFile, version: String?): Promise<Unit>

  fun addCommandToService(id: String, direction: String, event: SdkResourcePointer, version: String?): Promise<Unit>

  fun getQuery(id: String, version: String?): Promise<SdkMessage?>

  fun versionQuery(id: String): Promise<Unit>

  fun writeQuery(query: SdkMessage, options: WriteOptions): Promise<Unit>

  fun writeQueryToService(query: SdkMessage, service: SdkResourcePointer, options: WriteOptions): Promise<Unit>

  fun addFileToQuery(id: String, file: SdkFile, version: String?): Promise<Unit>

  fun addQuery(id: String, direction: String, event: SdkResourcePointer, version: String?): Promise<Unit>

  fun addQueryToService(id: String, direction: String, event: SdkResourcePointer, version: String?): Promise<Unit>

}