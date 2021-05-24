package org.tix.platform.io

import okio.FileSystem
import org.tix.platform.path.pathByExpandingTilde

class TextFileIO(private val fileSystem: FileSystem = FileSystem.SYSTEM): FileIO<String> {
    override fun read(path: String): String = fileSystem.read(path.pathByExpandingTilde()) { readUtf8() }

    override fun write(path: String, contents: String) {
        fileSystem.write(path.pathByExpandingTilde()) { writeUtf8(contents) }
    }
}