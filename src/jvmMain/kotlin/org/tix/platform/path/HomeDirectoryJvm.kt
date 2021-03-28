package org.tix.platform.path

internal actual val homeDirectory: String
    get() = System.getProperty("user.home")