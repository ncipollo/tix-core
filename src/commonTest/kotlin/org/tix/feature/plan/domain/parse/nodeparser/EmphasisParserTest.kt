package org.tix.feature.plan.domain.parse.nodeparser

import kotlin.test.Test
import kotlin.test.expect

class EmphasisParserTest {
    private val parser = EmphasisParser()

    @Test
    fun parse() {
        val arguments = "*text*".toParserArguments().childArguments!!
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments) { emphasis("text") }
        expect(1) { results.nextIndex }
    }
}