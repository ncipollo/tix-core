package org.tix.platform.io

import okio.FileSystem
import okio.Path

class TextFileIO(private val fileSystem: FileSystem = FileSystem.SYSTEM): FileIO<String> {
    override fun read(path: Path): String = fileSystem.read(path) { readUtf8() }

    override fun write(path: Path, contents: String) {
        fileSystem.write(path) { writeUtf8(contents) }
    }
}