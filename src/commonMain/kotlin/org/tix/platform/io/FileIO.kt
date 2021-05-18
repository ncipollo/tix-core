package org.tix.platform.io

interface FileIO<T> {
    fun read(path: String): T
    fun write(path: String, contents: T)
}