package org.tix.feature.plan.domain.parse.nodeparser

import kotlin.test.Test
import kotlin.test.expect

class CodeSpanParserTest {
    private val parser = CodeSpanParser()

    @Test
    fun parse() {
        val arguments = "`text`".toParserArguments().childArguments!!
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments) { codeSpan("text") }
        expect(1) { results.nextIndex }
    }
}