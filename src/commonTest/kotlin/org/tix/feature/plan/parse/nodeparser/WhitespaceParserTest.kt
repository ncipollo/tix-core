package org.tix.feature.plan.parse.nodeparser

import org.tix.model.ticket.body.BodySegment
import org.tix.model.ticket.body.WhitespaceSegment
import kotlin.test.Test
import kotlin.test.expect

class WhitespaceParserTest {
    private val parser = WhitespaceParser()

    @Test
    fun parse() {
        val arguments = "   ".toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expect(listOf<BodySegment>(WhitespaceSegment(count = 3))) { arguments.state.currentTicket!!.body }
        expect(1) { results.nextIndex }
    }
}