package org.tix.platform

import kotlinx.cinterop.toKString
import platform.posix.getenv

actual object Env {
    actual operator fun get(name: String) = getenv(name)?.toKString() ?: ""
}