package org.tix.platform.path

import platform.Foundation.NSHomeDirectory

internal actual val homeDirectory: String
    get() = NSHomeDirectory()