package org.tix.feature.plan.parse.state

import org.tix.model.ticket.Ticket
import org.tix.model.ticket.body.TextSegment
import org.tix.model.ticket.body.TicketBody
import kotlin.test.Test
import kotlin.test.expect

class PartialTicketTest {
    private val childTicket2 = PartialTicket(title = "child 2", body = mutableListOf(TextSegment()))
    private val childTicket1 = PartialTicket(
        title = "child 1",
        body = mutableListOf(TextSegment()),
        children = mutableListOf(childTicket2)
    )
    private val rootTicket = PartialTicket(
        title = "root",
        body = mutableListOf(TextSegment()),
        children = mutableListOf(childTicket1)
    )

    @Test
    fun ticket() {
        val expectedChild2 = Ticket(title = "child 2", body = TicketBody(listOf(TextSegment())))
        val expectedChild1 = Ticket(
            title = "child 1",
            body = TicketBody(listOf(TextSegment())),
            children = listOf(expectedChild2)
        )
        val expectedRoot = Ticket(
            title = "root",
            body = TicketBody(listOf(TextSegment())),
            children = listOf(expectedChild1)
        )
        expect(expectedRoot) { rootTicket.ticket() }
    }
}