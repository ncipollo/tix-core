package org.tix.feature.plan.parse.state

import org.tix.model.ticket.body.BlockQuoteSegment
import org.tix.model.ticket.body.LineBreakSegment
import org.tix.model.ticket.body.TextBlockSegment
import kotlin.test.Test
import kotlin.test.expect

class ParserStateTest {
    private val parserState = ParserState()

    @Test
    fun addBodySegments() {
        parserState.startTicket()
        parserState.addBodySegments(BlockQuoteSegment, TextBlockSegment)

        expect(listOf(BlockQuoteSegment, TextBlockSegment)) { parserState.currentTicket!!.body }
    }

    @Test
    fun addBodyLineBreak() {
        parserState.startTicket()
        parserState.addBodyLineBreak()

        expect(listOf(LineBreakSegment)) { parserState.currentTicket!!.body }
    }

    @Test
    fun completeTicket() {
        parserState.startTicket()
        parserState.startTicket()
        parserState.completeTicket()
        parserState.currentTicket?.title = "title"

        val expectedTicket = PartialTicket(title = "title", children = mutableListOf(PartialTicket()))
        expect(expectedTicket) { parserState.currentTicket }
    }

    @Test
    fun completeAllTickets() {
        parserState.startTicket()
        parserState.currentTicket?.title = "title"
        parserState.startTicket()
        parserState.completeAllTickets()

        val expectedTickets = mutableListOf(
            PartialTicket(title = "title", children = mutableListOf(PartialTicket()))
        )
        expect(expectedTickets) { parserState.rootTickets }
        expect(null) { parserState.currentTicket }
    }

    @Test
    fun currentTicket_hasNoStartedTicket() {
        expect(null) { parserState.currentTicket }
    }

    @Test
    fun currentTicket_hasStartedTicket() {
        parserState.startTicket()
        parserState.startTicket()
        parserState.currentTicket?.title = "title"

        expect(PartialTicket(title = "title")) { parserState.currentTicket }
    }

    @Test
    fun ticketLevel() {
        parserState.startTicket()
        parserState.startTicket()

        expect(2) { parserState.ticketLevel }
    }
}