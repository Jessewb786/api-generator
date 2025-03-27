import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin.Companion.kotlinNodeJsEnvSpec
import java.nio.file.Files
import kotlin.io.path.absolutePathString

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kotlinxSerialization)
}

val libraryName = "@fusionpowered/api-generator"

kotlin {
  js {
    outputModuleName = libraryName
    nodejs {
      version = "1.0.0"
      val main by compilations.getting {
        packageJson {
          main = "kotlin/index.js"
        }
      }

      kotlinNodeJsEnvSpec.download = true
      kotlinNodeJsEnvSpec.version = "22.14.0"
      kotlinNodeJsEnvSpec.installationDirectory = projectDir.resolve(".gradle")
    }
    compilerOptions {
      target = "es2015"
      optIn.addAll(
        "kotlin.js.ExperimentalJsExport",
        "kotlin.js.ExperimentalJsCollectionsApi",
        "kotlinx.coroutines.DelicateCoroutinesApi"
      )
    }
    binaries.executable()
  }

  sourceSets {
    jsMain.dependencies {
      implementation(libs.bundles.kotlinx)
      implementation(npm("@asyncapi/avro-schema-parser", "^3.0.24"))
      implementation(npm("@asyncapi/parser", "^3.3.0"))
      implementation(npm("chalk", "4"))
      implementation(npm("@apidevtools/swagger-parser", "^10.1.0"))
      implementation(npm("@eventcatalog/sdk", "2.2.3"))
      implementation(npm("js-yaml", "^4.1.0"))
      implementation(npm("slugify", "^1.6.6"))
      implementation(npm("node-fetch", "3.3.2"))
    }
  }
}

val npmPack = tasks.register<Exec>("npmPack") {
  val gradlePath = projectDir.resolve(".gradle").toPath()
  val npm = Files.find(gradlePath, 5, { path, _ -> path.fileName.endsWith("npm") })
    .filter { path -> path.absolutePathString().contains(kotlinNodeJsEnvSpec.version.get()) }
    .findFirst().get()
    .absolutePathString()
  commandLine = listOf(npm, "pack")
  workingDir = projectDir.resolve("build/js/packages/${libraryName}")
}

tasks.build {
  finalizedBy(npmPack)
}