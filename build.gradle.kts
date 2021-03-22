plugins {
    val kotlinVersion = "1.4.31"
    kotlin("multiplatform") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
}

group = "org.tix"
version = "0.0.1"

repositories {
    mavenCentral()
    jcenter()
    // Note: We are using a fork until this PR is merged: https://github.com/JetBrains/markdown/pull/59
    maven { setUrl("https://dl.bintray.com/drewcarlson/mordant") }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }

    val nativeTargets = listOf(linuxX64(), macosX64(), mingwX64())
    nativeTargets.forEach {
        it.apply {
            binaries {
                sharedLib {
                    baseName = "core"
                }
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.squareup.okio:okio-multiplatform:3.0.0-alpha.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
                implementation("org.jetbrains:markdown:0.2.0.pre-mpp")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0-RC")
                implementation("net.mamoe.yamlkt:yamlkt:0.9.0-dev-1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        val linuxX64Main by getting
        val linuxX64Test by getting

        val mingwX64Main by getting
        val mingwX64Test by getting

        val macosX64Main by getting
        val macosX64Test by getting

        val desktopMain by creating {
            dependsOn(commonMain)
            listOf(linuxX64Main, mingwX64Main, macosX64Main).forEach { it.dependsOn(this) }
        }
        val desktopTest by creating {
            dependsOn(commonMain)
            listOf(linuxX64Test, mingwX64Test, macosX64Test).forEach { it.dependsOn(this) }
        }
    }
}