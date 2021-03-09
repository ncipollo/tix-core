package org.tix.feature.plan.parse.nodeparser

import kotlin.test.Test
import kotlin.test.expect

class TextParserTest {
    private val parser = TextParser()

    @Test
    fun parse_normalText() {
        val arguments = "text".toParserArguments().childArguments!!
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments) { text("text") }
        expect(1) { results.nextIndex }
    }

    @Test
    fun parse_weirdTokens() {
        val arguments = "(".toParserArguments().childArguments!!
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments) { text("(") }
        expect(1) { results.nextIndex }
    }
}