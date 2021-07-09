package org.tix.platform

expect object Env {
    operator fun get(name: String): String
}