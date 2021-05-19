object Versions {
    const val coroutines = "1.4.3"
    const val json = "1.1.0-RC"
    const val koin = "3.0.1"
    const val kotlin = "1.4.31"
    const val markdown = "0.2.3"
    const val mockK = "1.11.0"
    const val okio = "3.0.0-alpha.1"
    const val turbine = "0.4.1"
    const val yml = "0.9.0-dev-1"
}

object Deps {
    val coroutines = dep("org.jetbrains.kotlinx:kotlinx-coroutines-core", Versions.coroutines)

    object Koin {
        val core = dep("io.insert-koin:koin-core", Versions.koin)
        val test = dep("io.insert-koin:koin-test", Versions.koin)
    }

    val markdown = dep("org.jetbrains:markdown", Versions.markdown)

    object MockK {
        val common = dep("io.mockk:mockk-common", Versions.mockK)
        val jvm = dep("io.mockk:mockk", Versions.mockK)
    }

    object Okio {
        val fakeFilesystem = dep("com.squareup.okio:okio-fakefilesystem-multiplatform", Versions.okio)
        val multiplatform = dep("com.squareup.okio:okio-multiplatform", Versions.okio)
    }

    object Serialization {
        val json = dep("org.jetbrains.kotlinx:kotlinx-serialization-json", Versions.json)
        val yml = dep("net.mamoe.yamlkt:yamlkt", Versions.yml)
    }

    val turbine = dep("app.cash.turbine:turbine", Versions.turbine)

    private fun dep(name: String, version: String) = "$name:$version"
}