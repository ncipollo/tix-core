package org.tix.ext

import kotlin.test.Test
import kotlin.test.expect

class NumberExtTest {
    @Test
    fun isWholeNumber_whenNumberIsFractional_returnsFalse() {
        expect(false) { 0.14.isWholeNumber() }
    }

    @Test
    fun isWholeNumber_whenNumberIsMixed_returnsFalse() {
        expect(false) { 3.14.isWholeNumber() }
    }

    @Test
    fun isWholeNumber_whenNumberIsWhole_returnsTrue() {
        expect(true) { 3.0.isWholeNumber() }
    }
}