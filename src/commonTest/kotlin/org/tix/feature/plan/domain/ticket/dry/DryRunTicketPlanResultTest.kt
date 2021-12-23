package org.tix.feature.plan.domain.ticket.dry

import kotlin.test.Test
import kotlin.test.expect

class DryRunTicketPlanResultTest {
    private val result = DryRunTicketPlanResult(
        id = "1",
        title = "Title",
        ticketType = "Epic",
        body = "Line 1\nLine 2"
    )

    @Test
    fun description() {
        val expected = """
            -----------------
            🚀 Epic - Title
            Line 1
            Line 2
            -----------------
        """.trimIndent()

        expect(expected) {
            result.description
        }
    }
}