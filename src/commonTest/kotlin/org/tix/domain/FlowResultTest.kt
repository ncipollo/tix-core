package org.tix.domain

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.expect

class FlowResultTest {
    @Test
    fun checkExpectedError_whenFailedResultWithExpectedErrorType_doesNotThrow() {
        val result = FlowResult.failure<Nothing>(IllegalStateException("not the worst"))
        expect(IllegalStateException::class) {
            result.checkExpectedError<IllegalStateException>()
            result.exceptionOrNull()!!::class
        }
    }

    @Test
    fun checkExpectedError_whenFailedResultWithUnexpectedErrorType_throws() {
        val result = FlowResult.failure<Nothing>(IllegalArgumentException("very much not awesome"))
        assertFailsWith<IllegalArgumentException> {
            result.checkExpectedError<IllegalStateException>()
        }
    }

    @Test
    fun checkExpectedError_whenSuccessfulResult_doesNotThrow() {
        val result = FlowResult.success("awesome")
        expect("awesome") {
            result.checkExpectedError<IllegalStateException>()
            result.getOrNull()!!
        }
    }

    @Test
    fun map_failure() {
        val exception = IllegalStateException("oh no")
        val result = FlowResult.failure<Int>(exception)
            .map { "the answer is not the play the game" }
        expect(exception) {
            result.exceptionOrNull()
        }
    }

    @Test
    fun map_success() {
        val result = FlowResult.success(42).map { "the answer is $it" }
        expect("the answer is 42") {
            result.getOrThrow()
        }
    }

    @Test
    fun map_thrownException() {
        val exception = RuntimeException("oh no")
        val result = FlowResult.success(42).map { throw exception }
        expect(exception) {
            result.exceptionOrNull()
        }
    }

}