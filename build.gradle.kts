@file:Suppress("UNUSED_VARIABLE")

plugins {
    val kotlinVersion = "1.4.31"
    kotlin("multiplatform") version Versions.kotlin
    kotlin("plugin.serialization") version Versions.kotlin
}

group = "org.tix"
version = "0.0.1"

repositories {
    mavenCentral {
        content {
            excludeGroup("net.mamoe.yamlkt")
        }
    }
    jcenter {
        content {
            includeGroup("net.mamoe.yamlkt")
        }
    }
}

kotlin {
    js {
        browser {
            webpackTask {
                outputFileName = "tix-core.js"
                output.libraryTarget = "commonjs2"
            }
        }
    }

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }

    listOf(linuxX64(), macosX64(), mingwX64()).forEach {
        it.apply {
            binaries {
                sharedLib {
                    baseName = "core"
                }
            }
        }
    }

    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("okio.ExperimentalFileSystem")
        }
        val commonMain by getting {
            dependencies {
                implementation(Deps.coroutines)
                implementation(Deps.markdown)
                implementation(Deps.Serialization.json)
                implementation(Deps.Serialization.yml)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(Deps.Okio.fakeFilesystem)
                implementation(Deps.MockK.common)
            }
        }

        val notWebMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(Deps.Okio.multiplatform)
            }
        }
        val notWebTest by creating { dependsOn(commonTest) }

        val jsMain by getting
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val jvmMain by getting { dependsOn(notWebMain) }
        val jvmTest by getting {
            dependencies {
                dependsOn(notWebTest)
                implementation(kotlin("test-junit"))
            }
        }

        val desktopMain by creating { dependsOn(notWebMain) }
        val desktopTest by creating { dependsOn(notWebTest) }
        val linuxX64Main by getting { dependsOn(desktopMain) }
        val macosX64Main by getting { dependsOn(desktopMain) }
        val mingwX64Main by getting { dependsOn(desktopMain) }
        val linuxX64Test by getting { dependsOn(desktopTest) }
        val macosX64Test by getting { dependsOn(desktopTest) }
        val mingwX64Test by getting { dependsOn(desktopTest) }
    }
}

tasks.withType<Test> {
    testLogging.showStandardStreams = true
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinTest> {
    testLogging.showStandardStreams = true
}

tasks.register("nativeTest") {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val testTaskName = when {
        hostOs == "Mac OS X" -> "macosX64Test"
        hostOs == "Linux" -> "linuxX64Test"
        isMingwX64 -> "mingwX64Test"
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }
    println("Running native tests: $testTaskName")
    dependsOn(tasks.findByName(testTaskName))
}