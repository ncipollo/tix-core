package org.tix.platform.path

import org.tix.node.homedir

internal actual val homeDirectory: String
    get() = homedir()