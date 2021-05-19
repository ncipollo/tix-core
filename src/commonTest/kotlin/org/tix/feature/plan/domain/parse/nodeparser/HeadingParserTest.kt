package org.tix.feature.plan.domain.parse.nodeparser

import org.tix.feature.plan.domain.parse.ParseException
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.expect

class HeadingParserTest {
    private val parser = HeadingParser()

    @Test
    fun parse_completesPreviousTickets_oneTicketLevelDifference_nodeLevelOne() {
        val arguments = "# Ticket 1".toParserArguments()
        arguments.state.startTicket()

        parser.parse(arguments)

        expect("Ticket 1") { arguments.state.currentTicket!!.title }
        expect(1) { arguments.state.ticketLevel }
        expect(1) { arguments.state.rootTickets.size }
        expect(0) { arguments.state.rootTickets[0].children.size }
    }

    @Test
    fun parse_completesPreviousTickets_oneTicketLevelDifference_nodeLevelTwo() {
        val arguments = "## Ticket 1".toParserArguments()
        arguments.state.startTicket()
        arguments.state.startTicket()

        parser.parse(arguments)

        expect("Ticket 1") { arguments.state.currentTicket!!.title }
        expect(2) { arguments.state.ticketLevel }
        arguments.state.completeAllTickets()
        expect(1) { arguments.state.rootTickets.size }
        expect(2) { arguments.state.rootTickets[0].children.size }
    }

    @Test
    fun parse_completesPreviousTickets_twoTicketLevelDifference() {
        val arguments = "# Ticket 1".toParserArguments()
        arguments.state.startTicket()
        arguments.state.startTicket()

        parser.parse(arguments)

        expect("Ticket 1") { arguments.state.currentTicket!!.title }
        expect(1) { arguments.state.ticketLevel }
        expect(1) { arguments.state.rootTickets.size }
        expect(1) { arguments.state.rootTickets[0].children.size }
    }

    @Test
    fun parse_noPreviousTickets() {
        val arguments = "# Ticket 1".toParserArguments()

        parser.parse(arguments)

        expect("Ticket 1") { arguments.state.currentTicket!!.title }
        expect(1) { arguments.state.ticketLevel }
        expect(0) { arguments.state.rootTickets.size }
    }

    @Test
    fun parse_noPreviousTickets_throwsWhenHeaderLevelTooLarge() {
        val arguments = "## Ticket 1".toParserArguments()

        assertFailsWith(ParseException::class) {
            parser.parse(arguments)
        }
    }

    @Test
    fun parse_noTitle_throws() {
        val arguments = "#".toParserArguments()

        assertFailsWith(ParseException::class) {
            parser.parse(arguments)
        }
    }

    @Test
    fun parse_returnsResultWhichDoesNotSkipNextNode() {
        val arguments = "# Ticket1".toParserArguments()
        expect(arguments.resultsFromArgs(1)) { parser.parse(arguments) }
    }

    @Test
    fun parse_returnsResultWhichSkipsNextNode() {
        val arguments = "# Ticket1\n".toParserArguments()
        expect(arguments.resultsFromArgs(2)) { parser.parse(arguments) }
    }

    @Test
    fun parse_withPreviousTickets_throwsWhenHeaderLevelTooLarge() {
        val arguments = "#### Ticket 1".toParserArguments()
        arguments.state.startTicket()

        assertFailsWith(ParseException::class) {
            parser.parse(arguments)
        }
    }
}