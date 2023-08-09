package org.tix.error

import kotlin.test.Test
import kotlin.test.expect

class TixErrorTest {
    @Test
    fun toTixError_alreadyATixError() {
        val tixError = TixError(message = "tix_error", mood = "😬")
        expect(tixError) {
            tixError.toTixError()
        }
    }

    @Test
    fun toTixError_otherError() {
        val cause = RuntimeException("cause")
        val tixError = RuntimeException("other_error", cause)
        expect(TixError(message = "other_error", cause = cause)) {
            tixError.toTixError()
        }
    }
}