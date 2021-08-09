package org.tix.platform.fs

import okio.FileSystem
import okio.NodeJsFileSystem

actual val defaultFileSystem: FileSystem
    get() = NodeJsFileSystem