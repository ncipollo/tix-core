package org.tix.platform

actual object PlatformEnv : Env {
    actual override operator fun get(name: String): String = System.getenv(name)
}