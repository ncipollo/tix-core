package org.tix.feature.plan.domain.parse

import io.ktor.utils.io.core.*
import okio.FileSystem
import okio.fakefilesystem.FakeFileSystem
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class MarkdownSourceValidatorTest {
    private val fileSystem = FakeFileSystem()
    private val markdownSourceValidator = MarkdownSourceValidator(fileSystem)

    private val path = FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "test.md"

    @AfterTest
    fun after() = fileSystem.checkNoOpenFiles()

    @Test
    fun validate_file_missingFile_throws() {
        fileSystem.createDirectories(path.parent!!)
        assertFailsWith<MarkdownSourceException> {
            markdownSourceValidator.validate(MarkdownFileSource(path.toString()))
        }
    }

    @Test
    fun validate_file_pathIsDirectory_throws() {
        fileSystem.createDirectories(path)
        assertFailsWith<MarkdownSourceException> {
            markdownSourceValidator.validate(MarkdownFileSource(path.toString()))
        }
    }

    @Test
    fun validate_file_validFile() {
        fileSystem.createDirectories(path.parent!!)
        fileSystem.write(path) {
            write("test".toByteArray())
        }
        markdownSourceValidator.validate(MarkdownFileSource(path.toString()))
    }

    @Test
    fun validate_text_hasContent() {
        markdownSourceValidator.validate(MarkdownTextSource("# Title"))
    }

    @Test
    fun validate_text_noContent_throws() {
        assertFailsWith<MarkdownSourceException> {
            markdownSourceValidator.validate(MarkdownTextSource(" "))
        }
    }
}