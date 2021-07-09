package org.tix.platform

import kotlinx.cinterop.toKString
import platform.posix.getenv

actual object PlatformEnv : Env {
    actual override operator fun get(name: String) = getenv(name)?.toKString() ?: ""
}