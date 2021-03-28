package org.tix.platform.path

import kotlinx.cinterop.pointed
import kotlinx.cinterop.toKString
import platform.posix.getpwuid
import platform.posix.getuid

internal actual val homeDirectory: String
    get() = getpwuid(getuid())?.pointed?.pw_dir?.toKString() ?: noHomeError()