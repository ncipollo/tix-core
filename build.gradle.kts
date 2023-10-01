@file:Suppress("UNUSED_VARIABLE")

import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.vanniktech.maven.publish.SonatypeHost

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
    val kotlinVersion = "1.9.10"
    kotlin("multiplatform") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("com.codingfeline.buildkonfig") version "0.13.3"
    id("com.vanniktech.maven.publish") version "0.25.3"
    jacoco
}

repositories {
    mavenCentral()
}

kotlin {
    js(IR) {
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
            kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
        }

        val test by compilations.getting {
            jacoco {
                toolVersion = "0.8.7"
            }

            tasks.register<JacocoReport>("jacocoTestReport") {
                val coverageSourceDirs = arrayOf(
                    "src/commonMain/kotlin",
                    "src/jvmMain/kotlin"
                )

                val classFiles = File("${buildDir}/classes/kotlin/jvm/")
                    .walkBottomUp()
                    .toSet()

                classDirectories.setFrom(classFiles)
                sourceDirectories.setFrom(files(coverageSourceDirs))

                executionData.setFrom(files("${buildDir}/jacoco/jvmTest.exec"))

                reports {
                    xml.required.set(true)
                    html.required.set(true)
                }
            }

            tasks.register<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
                val coverageSourceDirs = arrayOf(
                    "src/commonMain/kotlin",
                    "src/jvmMain/kotlin"
                )

                val classFiles = File("${buildDir}/classes/kotlin/jvm/")
                    .walkBottomUp()
                    .toSet()

                classDirectories.setFrom(classFiles)
                sourceDirectories.setFrom(files(coverageSourceDirs))

                executionData.setFrom(files("${buildDir}/jacoco/jvmTest.exec"))

                violationRules {
                    rule {
                        limit {
                            minimum = "0.6".toBigDecimal()
                        }
                    }
                }
            }
        }

        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        linuxX64(),
        macosX64(),
        macosArm64()
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
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            languageSettings.optIn("kotlinx.coroutines.FlowPreview")
            // This is required for Turbine
            languageSettings.optIn("kotlin.time.ExperimentalTime")
        }

        val commonMain by getting {
            dependencies {
                api(libs.coroutine.core)
                implementation(libs.kotlin.time)
                implementation(libs.ktor.auth)
                implementation(libs.ktor.content.negotation)
                implementation(libs.ktor.core)
                implementation(libs.ktor.serialization)
                implementation(libs.markdown)
                implementation(libs.okio.multiplatform)
                implementation(libs.serialization.json)
                implementation(libs.serialization.yml)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("reflect"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.coroutine.test)
                implementation(libs.ktor.client.mock)
                implementation(libs.turbine)
                implementation(libs.okio.fake.filesystem)
            }
        }

        val nativeMain by creating { dependsOn(commonMain) }
        val nativeTest by creating { dependsOn(commonTest) }

        val jsMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.ktor.js)
                implementation(libs.okio.node)
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
                implementation(libs.ktor.jvm)
            }
        }
        val jvmTest by getting {
            dependencies {
                dependsOn(commonTest)
                implementation(kotlin("test-junit"))
                implementation(libs.mockk.jvm)
            }
        }

        val iosMain by creating {
            dependsOn(nativeMain)
            dependencies {
                implementation(libs.ktor.ios)
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
                implementation(libs.ktor.curl)
            }
        }
        val desktopTest by creating { dependsOn(nativeTest) }
        val linuxX64Main by getting { dependsOn(desktopMain) }
        val linuxX64Test by getting { dependsOn(desktopTest) }

        val macosMain by creating { dependsOn(desktopMain) }
        val macosTest by creating { dependsOn(desktopTest) }

        val macosArm64Main by getting { dependsOn(macosMain) }
        val macosArm64Test by getting { dependsOn(macosTest) }
        val macosX64Main by getting { dependsOn(macosMain) }
        val macosX64Test by getting { dependsOn(macosTest) }
    }
}

// Publishing
group = "io.github.ncipollo.tix"
version = System.getenv("TIX_VERSION") ?: "1.0.0-SNAPSHOT"

buildkonfig {
    packageName = "org.tix.config"
    exposeObjectWithName = "TixCoreConfig"
    defaultConfigs {
        buildConfigField(STRING, "version", version.toString())
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

tasks.register("printCoverageLocation") {
    val htmlIndexPath = "${buildDir}/reports/jacoco/jacocoTestReport/html/index.html"
    println("Coverage HTML Location: $htmlIndexPath")
}

tasks.register("jvmTestCoverage") {
    dependsOn("jvmTest")
    finalizedBy("jacocoTestReport")
    finalizedBy("jacocoTestCoverageVerification")
    finalizedBy("printCoverageLocation")
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.S01, true)
    signAllPublications()

    coordinates(group.toString(), name, version.toString())

    // Provide artifacts information requited by Maven Central
    pom {
        name.set("Tix Core Library")
        description.set("Kotlin MPP library for authoring, tracking and managing tickets.")
        url.set("https://github.com/ncipollo/tix-core")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("ncipollo")
                name.set("Nick Cipollo")
                email.set("njc115@gmail.com>")
            }
        }
        scm {
            url.set("https://github.com/ncipollo/tix-core.git")
        }
    }
}