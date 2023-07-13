object Versions {
    const val coroutines = "1.7.2"
    const val json = "1.5.0"
    const val kotlin = "1.8.21"
    const val ktor = "2.2.1"
    const val markdown = "0.4.1"
    const val mockK = "1.11.0"
    const val okio = "3.2.0"
    const val turbine = "1.0.0"
    const val yml = "0.13.0"
}

object Deps {
    object Coroutines {
        val core = dep("org.jetbrains.kotlinx:kotlinx-coroutines-core", Versions.coroutines)
        val test = dep("org.jetbrains.kotlinx:kotlinx-coroutines-test", Versions.coroutines)
    }

    object Ktor {
        val android = dep("io.ktor:ktor-client-android", Versions.ktor)
        val auth = dep("io.ktor:ktor-client-auth", Versions.ktor)
        val clientMock = dep("io.ktor:ktor-client-mock", Versions.ktor)
        val contentNegotation = dep("io.ktor:ktor-client-content-negotiation", Versions.ktor)
        val core = dep("io.ktor:ktor-client-core", Versions.ktor)
        val curl = dep("io.ktor:ktor-client-curl", Versions.ktor)
        val iOS = dep("io.ktor:ktor-client-ios", Versions.ktor)
        val js = dep("io.ktor:ktor-client-js", Versions.ktor)
        val jvm = dep("io.ktor:ktor-client-java", Versions.ktor)
        val serialization = dep("io.ktor:ktor-serialization-kotlinx-json", Versions.ktor)
    }

    val markdown = dep("org.jetbrains:markdown", Versions.markdown)

    object MockK {
        val common = dep("io.mockk:mockk-common", Versions.mockK)
        val jvm = dep("io.mockk:mockk", Versions.mockK)
    }

    object Okio {
        val fakeFilesystem = dep("com.squareup.okio:okio-fakefilesystem", Versions.okio)
        val multiplatform = dep("com.squareup.okio:okio", Versions.okio)
        val nodeFilesystem = dep("com.squareup.okio:okio-nodefilesystem", Versions.okio)
        // Workaround for okio's use of kotlin time
        val kotlinTime = dep("org.jetbrains.kotlinx:kotlinx-datetime", "0.4.0")
    }

    object Serialization {
        val json = dep("org.jetbrains.kotlinx:kotlinx-serialization-json", Versions.json)
        val yml = dep("net.mamoe.yamlkt:yamlkt", Versions.yml)
    }

    val turbine = dep("app.cash.turbine:turbine", Versions.turbine)

    private fun dep(name: String, version: String) = "$name:$version"
}