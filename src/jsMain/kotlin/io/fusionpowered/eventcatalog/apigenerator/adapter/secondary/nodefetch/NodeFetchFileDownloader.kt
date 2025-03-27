package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.nodefetch

import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.nodefetch.jsModule.fetch
import io.fusionpowered.eventcatalog.apigenerator.port.FileDownloader
import kotlinx.coroutines.await

class NodeFetchFileDownloader : FileDownloader {

  override suspend fun download(url: String): String? {
    return fetch(url).await()
      .text().await()
  }

}