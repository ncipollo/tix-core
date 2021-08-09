package org.tix.platform

import org.tix.node.process

actual object PlatformEnv : Env {
    actual override operator fun get(name: String) = process.env[name] ?: ""
}