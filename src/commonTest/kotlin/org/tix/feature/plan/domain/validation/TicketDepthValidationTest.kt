package org.tix.feature.plan.domain.validation

import org.tix.feature.plan.domain.stats.jiraTicketStats
import org.tix.ticket.Ticket
import kotlin.test.Test
import kotlin.test.assertFails

class TicketDepthValidationTest {
    private val levelLabels = jiraTicketStats(noEpics = true)

    private val grandChild = Ticket("grandchild")
    private val child = Ticket("child", children = listOf(grandChild))

    @Test
    fun validate_noError() {
        val validation = TicketDepthValidation(levelLabels)
        val tickets = listOf(
            Ticket("ticket1"),
            Ticket("ticket2", children = listOf(grandChild))
        )

        // Shouldn't throw
        validation.validate(tickets)
    }

    @Test
    fun validate_throws_firstChildFails() {
        val validation = TicketDepthValidation(levelLabels)
        val tickets = listOf(
            Ticket("ticket1", children = listOf(child)),
            Ticket("ticket2")
        )

        assertFails {
            validation.validate(tickets)
        }
    }

    @Test
    fun validate_throws_secondChildFails() {
        val validation = TicketDepthValidation(levelLabels)
        val tickets = listOf(
            Ticket("ticket1"),
            Ticket("ticket2", children = listOf(child))
        )

        // Shouldn't throw
        assertFails {
            validation.validate(tickets)
        }
    }
}