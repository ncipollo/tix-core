package org.tix.platform

import org.tix.node.process

actual object PlatformEnv : Env {
    actual override operator fun get(name: String): String = process.env[name]?.toString() ?: ""
}