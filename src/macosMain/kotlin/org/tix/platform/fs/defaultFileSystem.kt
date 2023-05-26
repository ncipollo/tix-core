package org.tix.platform.fs

import okio.FileSystem

actual val defaultFileSystem: FileSystem
    get() = FileSystem.SYSTEM