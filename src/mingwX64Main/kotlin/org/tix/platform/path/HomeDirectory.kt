package org.tix.platform.path

import kotlinx.cinterop.toKString
import platform.posix.getenv

internal actual val homeDirectory: String
    get() = getenv("USERPROFILE")?.toKString() ?: noHomeError()