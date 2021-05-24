package org.tix.ext

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.expect

class ResultExtTest {
    @Test
    fun checkExpectedError_whenFailedResultWithExpectedErrorType_doesNotThrow() {
        val result = kotlin.runCatching { throw IllegalStateException("not the worst") }
        expect(IllegalStateException::class) {
            result.checkExpectedError<IllegalStateException>()
            result.exceptionOrNull()!!::class
        }
    }

    @Test
    fun checkExpectedError_whenFailedResultWithUnexpectedErrorType_throws() {
        val result = kotlin.runCatching { throw IllegalArgumentException("very much not awesome") }
        assertFailsWith<IllegalArgumentException> {
            result.checkExpectedError<IllegalStateException>()
        }
    }

    @Test
    fun checkExpectedError_whenSuccessfulResult_doesNotThrow() {
        val result = kotlin.runCatching { "awesome" }
        expect("awesome") {
            result.checkExpectedError<IllegalStateException>()
            result.getOrNull()!!
        }
    }
}