package org.tix.feature.plan.parse.nodeparser

import kotlin.test.Test
import kotlin.test.expect

class WhitespaceParserTest {
    private val parser = WhitespaceParser()

    @Test
    fun parse() {
        val arguments = "   ".toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments) { whitespace(count = 3) }
        expect(1) { results.nextIndex }
    }
}