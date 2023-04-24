plugins {
    `maven-publish`
    signing
}

group = "io.github.ncipollo.tix"
version = System.getenv("TIX_VERSION") ?: "0.1.0-SNAPSHOT"

// Pull in OSSRH credentials from the environement
ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

fun getExtraString(name: String) = ext[name]?.toString()

val publishingUrl get() =
    if(version.toString().endsWith("SNAPSHOT")) {
        "https://s01.oss.sonatype.org/content/repositories/snapshots/"
    } else {
        "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
    }

publishing {
    // Configure maven central repository
    repositories {
        maven {
            name = "sonatype"
            setUrl(publishingUrl)
            credentials {
                username = getExtraString("ossrhUsername")
                password = getExtraString("ossrhPassword")
            }
        }
    }

    // Configure all publications
    publications.withType<MavenPublication> {
        // Stub javadoc.jar artifact
        artifact(javadocJar.get())

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
}

// Signing artifacts. Signing.* extra properties values will be used
signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}