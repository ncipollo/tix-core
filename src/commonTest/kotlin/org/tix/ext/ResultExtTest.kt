package org.tix.ext

import org.tix.domain.FlowResult
import kotlin.test.Test
import kotlin.test.expect

class ResultExtTest {
    @Test
    fun toFlowResult_whenResultIsFailure_returnsFlowFailure() {
        val exception = IllegalArgumentException("very much not awesome")
        val expected = FlowResult.failure<String>(exception)
        expect(expected) {
            val thing = Result.failure<String>(exception).toFlowResult()
            thing
        }
    }

    @Test
    fun toFlowResult_whenResultIsSuccess_returnsFlowSuccess() {
        val expected = FlowResult.success("awesome")
        expect(expected) {
            Result.success("awesome").toFlowResult()
        }
    }
}