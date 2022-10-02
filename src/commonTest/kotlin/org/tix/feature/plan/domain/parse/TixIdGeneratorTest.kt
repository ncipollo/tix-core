package org.tix.feature.plan.domain.parse

import org.tix.ticket.Ticket
import kotlin.test.Test
import kotlin.test.expect

class TixIdGeneratorTest {
    private val tickets = listOf(
        Ticket(
            title = "1",
            children = listOf(
                Ticket("2"),
                Ticket(title = "3", children = listOf(Ticket("4")))
            )
        ),
        Ticket(
            title = "5",
            children = listOf(
                Ticket("6"),
                Ticket(title = "7", children = listOf(Ticket("8")))
            )
        )
    )

    @Test
    fun attachIdsToTickets_defaultStartingId() {
        val generator = TixIdGenerator()
        val expected = listOf(
            Ticket(
                title = "1",
                tixId = "tix_1",
                children = listOf(
                    Ticket("2", tixId = "tix_2",),
                    Ticket(title = "3", tixId = "tix_3", children = listOf(Ticket("4", tixId = "tix_4",)))
                )
            ),
            Ticket(
                title = "5",
                tixId = "tix_5",
                children = listOf(
                    Ticket("6", tixId = "tix_6",),
                    Ticket(title = "7", tixId = "tix_7", children = listOf(Ticket("8", tixId = "tix_8",)))
                )
            )
        )
        expect(expected) {
            generator.attachIdsToTickets(tickets)
        }
    }

    @Test
    fun attachIdsToTickets_specifiedStartingId() {
        val generator = TixIdGenerator(startingId = 11)
        val expected = listOf(
            Ticket(
                title = "1",
                tixId = "tix_11",
                children = listOf(
                    Ticket("2", tixId = "tix_12",),
                    Ticket(title = "3", tixId = "tix_13", children = listOf(Ticket("4", tixId = "tix_14",)))
                )
            ),
            Ticket(
                title = "5",
                tixId = "tix_15",
                children = listOf(
                    Ticket("6", tixId = "tix_16",),
                    Ticket(title = "7", tixId = "tix_17", children = listOf(Ticket("8", tixId = "tix_18",)))
                )
            )
        )
        expect(expected) {
            generator.attachIdsToTickets(tickets)
        }
    }
}