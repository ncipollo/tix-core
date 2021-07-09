package org.tix.platform

actual object Env {
    actual operator fun get(name: String): String = System.getenv(name)
}