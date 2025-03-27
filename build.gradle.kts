import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin.Companion.kotlinNodeJsEnvSpec
import java.nio.file.Files
import kotlin.io.path.absolutePathString

plugins {
  alias(libs.plugins.kotlinMultiplatform)
}

val libraryName = "@fusionpowered/api-generator"

kotlin {
  js {
    outputModuleName = libraryName

    useEsModules()

    compilerOptions {
      target = "es2015"
    }

    nodejs {
      version = "1.0.0"
      val main by compilations.getting {
        packageJson {
          main = "kotlin/index.js"
        }

        dependencies {
          implementation(npm("chalk", "4"))
        }
      }

      kotlinNodeJsEnvSpec.download = true
      kotlinNodeJsEnvSpec.version = "22.14.0"
      kotlinNodeJsEnvSpec.installationDirectory = projectDir.resolve(".gradle")
    }

    generateTypeScriptDefinitions()

    binaries.executable()
  }
}

tasks.register<Exec>("npmPack") {
  dependsOn("build")
  val gradlePath = projectDir.resolve(".gradle").toPath()
  val npm = Files.find(gradlePath, 5, { path, _ -> path.fileName.endsWith("npm") })
    .filter { path -> path.absolutePathString().contains(kotlinNodeJsEnvSpec.version.get()) }
    .findFirst().get()
    .absolutePathString()
  commandLine = listOf(npm, "pack")
  workingDir = projectDir.resolve("build/js/packages/${libraryName}")
}

