@file:Suppress("UNUSED_VARIABLE")

/**
 * Dependency Hierarchy
 * ```
 * common
 * |-- js
 * '-- notWeb
 *     |-- jvm
 *     '-- native
 *         |-- iOS
 *             | -- iOSX64
 *             | -- iOSArm64
 *         |-- Desktop
 *             |--linuxX64
 *             |--macOS64
 *             |--mingw64 (windows)
 * ```
 */

plugins {
    kotlin("multiplatform") version Versions.kotlin
    kotlin("plugin.serialization") version Versions.kotlin
}

group = "org.tix"
version = "0.0.1"

repositories {
    mavenCentral()
}

kotlin {
    js {
        compilations.all {
            kotlinOptions {
                moduleKind = "umd"
                sourceMap = true
                metaInfo = true
            }
        }
        nodejs {
            testTask {
                useMocha {
                    timeout = "30s"
                }
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

    listOf(
        iosX64(),
        iosArm64(),
        linuxX64(),
        macosX64()
    ).forEach {
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
            languageSettings.useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
            languageSettings.useExperimentalAnnotation("kotlinx.coroutines.FlowPreview")
            // This is required for Turbine
            languageSettings.useExperimentalAnnotation("kotlin.time.ExperimentalTime")
        }
//        The following snippet causes the IDE to exception, we should test it again in the next IDEA version.
//        kotlin.sourceSets.matching { it.name.endsWith("Test") }.configureEach {
//            languageSettings.useExperimentalAnnotation("kotlin.time.ExperimentalTime")
//        }

        val commonMain by getting {
            dependencies {
                api(Deps.coroutines) {
                    version {
                        strictly(Versions.coroutines)
                    }
                }
                implementation(Deps.Ktor.auth)
                implementation(Deps.Ktor.core)
                implementation(Deps.Ktor.serialization)
                implementation(Deps.markdown)
                implementation(Deps.Okio.multiplatform)
                implementation(Deps.Serialization.json)
                implementation(Deps.Serialization.yml)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("reflect"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(Deps.turbine)
                implementation(Deps.Okio.fakeFilesystem)
            }
        }

        val nativeMain by creating { dependsOn(commonMain) }
        val nativeTest by creating { dependsOn(commonTest) }

        val jsMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(Deps.Ktor.js)
                implementation(Deps.Okio.nodeFilesystem)
            }
        }
        val jsTest by getting {
            dependsOn(commonTest)
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val jvmMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(Deps.Ktor.jvm)
            }
        }
        val jvmTest by getting {
            dependencies {
                dependsOn(commonTest)
                implementation(kotlin("test-junit"))
                implementation(Deps.MockK.jvm)
            }
        }

        val iosMain by creating {
            dependsOn(nativeMain)
            dependencies {
                implementation(Deps.Ktor.iOS)
            }
        }
        val iosTest by creating { dependsOn(nativeTest) }
        val iosArm64Main by getting { dependsOn(iosMain) }
        val iosArm64Test by getting { dependsOn(iosTest) }
        val iosX64Main by getting { dependsOn(iosMain) }
        val iosX64Test by getting { dependsOn(iosTest) }

        val desktopMain by creating {
            dependsOn(nativeMain)
            dependencies {
                implementation(Deps.Ktor.curl)
            }
        }
        val desktopTest by creating { dependsOn(nativeTest) }
        val linuxX64Main by getting { dependsOn(desktopMain) }
        val macosX64Main by getting { dependsOn(desktopMain) }
        val linuxX64Test by getting { dependsOn(desktopTest) }
        val macosX64Test by getting { dependsOn(desktopTest) }
    }
}

tasks.withType<Test> {
    testLogging.showStandardStreams = true
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinTest> {
    testLogging.showStandardStreams = true
}

// This task will run the native tests for the operating system you are running.
tasks.register("hostOSNativeTest") {
    val hostOs = System.getProperty("os.name")
    val testTaskName = when {
        hostOs == "Mac OS X" -> "macosX64Test"
        hostOs == "Linux" -> "linuxX64Test"
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }
    println("Running native tests: $testTaskName")
    dependsOn(tasks.findByName(testTaskName))
}