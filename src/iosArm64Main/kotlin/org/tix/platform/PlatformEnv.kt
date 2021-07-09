package org.tix.platform

import platform.Foundation.NSProcessInfo

actual object PlatformEnv: Env {
    actual override operator fun get(name: String) = NSProcessInfo.processInfo.environment[name]?.toString() ?: ""
}