package org.tix.feature.plan.domain.ticket

import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.expect

class TicketNumberTest {
    @Test
    fun ticketNumber_invalidKey() {
        assertFails { ticketNumber("invalid") }
    }

    @Test
    fun ticketNumber_withHashPrefix() {
        expect(42) { ticketNumber("#42") }
    }

    @Test
    fun ticketNumber_withoutHashPrefix() {
        expect(42) { ticketNumber("42") }
    }
}