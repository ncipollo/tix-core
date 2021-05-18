package org.tix.platform.io

actual class TextFileIO : FileIO<String> {
    override fun read(path: String): String = ""

    override fun write(path: String, contents: String) = Unit
}