package org.tix.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

@OptIn(ExperimentalForeignApi::class)
actual object PlatformEnv : Env {
    actual override operator fun get(name: String) = getenv(name)?.toKString() ?: ""
}