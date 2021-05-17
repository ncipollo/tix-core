package org.tix.platform.io

import okio.Path

interface FileIO<T> {
    fun read(path: Path): T
    fun write(path: Path, contents: T)
}