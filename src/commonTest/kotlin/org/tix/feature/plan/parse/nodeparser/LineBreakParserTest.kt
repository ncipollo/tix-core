package org.tix.feature.plan.parse.nodeparser

import org.tix.model.ticket.body.BodySegment
import org.tix.model.ticket.body.LineBreakSegment
import kotlin.test.Test
import kotlin.test.expect

class LineBreakParserTest {
    private val parser = LineBreakParser()

    @Test
    fun parse() {
        val arguments = "\n".toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expect(listOf<BodySegment>(LineBreakSegment)) { arguments.state.currentTicket!!.body }
        expect(1) { results.nextIndex }
    }
}