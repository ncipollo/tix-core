package org.tix.feature.plan.parse.nodeparser

import kotlin.test.Test
import kotlin.test.expect

class LinkParserTest {
    private val parser = LinkParser()

    @Test
    fun parse() {
        val arguments = "[title](https://api.example.com)".toParserArguments().childArguments!!
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments) { link("https://api.example.com", "title") }
        expect(1) { results.nextIndex }
    }
}