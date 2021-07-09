package org.tix.platform

interface Env {
    operator fun get(name: String): String
}

expect object PlatformEnv : Env {
    override operator fun get(name: String): String
}