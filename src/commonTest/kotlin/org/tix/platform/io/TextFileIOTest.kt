package org.tix.platform.io

import okio.FileSystem
import okio.fakefilesystem.FakeFileSystem
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.expect

class TextFileIOTest {
    private companion object {
        const val CONTENTS = "test contents"
    }

    private val fileSystem = FakeFileSystem()
    private val path = FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "test.txt"
    private val io = TextFileIO(fileSystem)

    @BeforeTest
    fun before() = fileSystem.createDirectories(path.parent!!)

    @AfterTest
    fun after() = fileSystem.checkNoOpenFiles()

    @Test
    fun read_afterWriting() {
        io.write(path, CONTENTS)
        expect(CONTENTS) { io.read(path) }
    }
}