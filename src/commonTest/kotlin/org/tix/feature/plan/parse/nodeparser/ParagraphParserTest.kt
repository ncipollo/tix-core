package org.tix.feature.plan.parse.nodeparser

import org.tix.model.ticket.body.*
import kotlin.test.Test
import kotlin.test.expect

class ParagraphParserTest {
    private val parser = ParagraphParser(NodeParserMap())

    @Test
    fun parse() {
        val arguments = "line1\nline2".toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        val expectedSegment = ParagraphSegment(
            listOf(
                TextSegment("line1"),
                LineBreakSegment,
                TextSegment("line2")
            ).toTicketBody()
        )
        expect(listOf<BodySegment>(expectedSegment)) { arguments.state.currentTicket!!.body }
        expect(1) { results.nextIndex }
    }
}