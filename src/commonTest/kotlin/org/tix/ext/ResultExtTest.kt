package org.tix.ext

import okio.IOException
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.expect

class ResultExtTest {
    @Test
    fun checkExpectedError_whenFailedResultWithExpectedErrorType_doesNotThrow() {
        val result = kotlin.runCatching { throw IOException("not the worst") }
        expect(IOException::class) {
            result.checkExpectedError<IOException>()
            result.exceptionOrNull()!!::class
        }
    }

    @Test
    fun checkExpectedError_whenFailedResultWithUnexpectedErrorType_throws() {
        val result = kotlin.runCatching { throw IllegalStateException("very much not awesome") }
        assertFailsWith<IllegalStateException> {
            result.checkExpectedError<IOException>()
        }
    }

    @Test
    fun checkExpectedError_whenSuccessfulResult_doesNotThrow() {
        val result = kotlin.runCatching { "awesome" }
        expect("awesome") {
            result.checkExpectedError<IOException>()
            result.getOrNull()!!
        }
    }
}