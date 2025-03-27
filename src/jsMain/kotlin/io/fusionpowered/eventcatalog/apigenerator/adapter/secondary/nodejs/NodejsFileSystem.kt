package io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.nodejs

import io.fusionpowered.eventcatalog.apigenerator.adapter.secondary.nodejs.jsModule.Fs
import io.fusionpowered.eventcatalog.apigenerator.port.FileSystem

class NodejsFileSystem : FileSystem {

  override fun readFile(path: String): String? {
    return Fs.readFileSync(path, "utf8")
  }

}