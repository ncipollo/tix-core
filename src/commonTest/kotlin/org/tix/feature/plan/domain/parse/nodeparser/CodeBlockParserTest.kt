package org.tix.feature.plan.domain.parse.nodeparser

import kotlin.test.Test
import kotlin.test.expect

class CodeBlockParserTest {
    private val parser = CodeBlockParser()

    @Test
    fun parse_() {
        val arguments = "    line1\n    line2\n".toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments) {codeBlock(code = "    line1\n    line2")}
        expect(1) { results.nextIndex }
    }
}