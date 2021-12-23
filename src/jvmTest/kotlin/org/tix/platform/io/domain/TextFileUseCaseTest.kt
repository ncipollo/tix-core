package org.tix.platform.io.domain

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.tix.domain.transform
import org.tix.platform.io.FileIO
import java.io.IOException
import kotlin.test.assertEquals

class TextFileUseCaseTest {
    private companion object {
        const val CONTENTS1 = "contents1"
        const val CONTENTS2 = "contents2"
        val ERROR = IOException()
    }

    private val fileIO = mockk<FileIO<String>>()
    private val useCase = TextFileUseCase(fileIO)

    private val source = listOf("file1.txt", "file2.txt").asFlow().transform(useCase)

    @Test
    fun transformFlow_whenFileReadFails_emitErrorThenResumes() = runTest {
        every { fileIO.read("file1.txt") } throws ERROR
        every { fileIO.read("file2.txt") } returns CONTENTS2

        source.test {
            assertEquals(ERROR, awaitItem().exceptionOrNull())
            assertEquals(CONTENTS2, awaitItem().getOrThrow())
            awaitComplete()
        }
    }

    @Test
    fun transformFlow_whenFileReadSucceeds_emitSuccess() = runTest {
        every { fileIO.read("file1.txt") } returns CONTENTS1
        every { fileIO.read("file2.txt") } returns CONTENTS2

        source.test {
            assertEquals(CONTENTS1, awaitItem().getOrThrow())
            assertEquals(CONTENTS2, awaitItem().getOrThrow())
            awaitComplete()
        }
    }
}