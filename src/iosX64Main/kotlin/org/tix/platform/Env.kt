package org.tix.platform

import platform.Foundation.NSProcessInfo

actual object Env {
    actual operator fun get(name: String): String = NSProcessInfo.processInfo.environment[name]?.toString() ?: ""
}