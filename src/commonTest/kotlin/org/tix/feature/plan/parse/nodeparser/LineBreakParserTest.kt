package org.tix.feature.plan.parse.nodeparser

import kotlin.test.Test
import kotlin.test.expect

class LineBreakParserTest {
    private val parser = LineBreakParser()

    @Test
    fun parse() {
        val arguments = "\n".toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments.state.currentTicket) { linebreak() }
        expect(1) { results.nextIndex }
    }
}