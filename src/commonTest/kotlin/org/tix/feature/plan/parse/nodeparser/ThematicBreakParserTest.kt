package org.tix.feature.plan.parse.nodeparser

import kotlin.test.Test
import kotlin.test.expect

class ThematicBreakParserTest {
    private val parser = ThematicBreakParser()

    @Test
    fun parse() {
        val arguments = "---".toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments) { thematicBreak() }
        expect(1) { results.nextIndex }
    }
}