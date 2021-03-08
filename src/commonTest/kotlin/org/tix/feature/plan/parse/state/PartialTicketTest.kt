package org.tix.feature.plan.parse.state

import org.tix.model.ticket.Ticket
import org.tix.model.ticket.body.BodySegment
import org.tix.model.ticket.body.LinebreakSegment
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
    fun addSegments() {
        rootTicket.addSegments(arrayOf(LinebreakSegment))
        expect(listOf(TextSegment(), LinebreakSegment)) { rootTicket.body }
    }

    @Test
    fun buildNestedBody() {
        val outerBody = rootTicket.buildNestedBody {
            rootTicket.addSegments(arrayOf(LinebreakSegment))
            val innerBody = rootTicket.buildNestedBody {
                rootTicket.addSegments(arrayOf(TextSegment("inner")))
            }
            expect(listOf<BodySegment>(TextSegment("inner"))) { innerBody }
        }
        expect(listOf<BodySegment>(LinebreakSegment)) { outerBody }
        expect(listOf<BodySegment>(TextSegment())) { rootTicket.body }
    }

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