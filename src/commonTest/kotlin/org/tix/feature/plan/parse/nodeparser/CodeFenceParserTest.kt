package org.tix.feature.plan.parse.nodeparser

import kotlin.test.Test
import kotlin.test.expect

class CodeFenceParserTest {
    private val parser = CodeFenceParser()

    @Test
    fun parse_withLanguage() {
        val arguments = """
            ```tix
            code goes here
            *next line*
            ```
        """.trimIndent().toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments) {
            codeBlock(code = "\ncode goes here\n*next line*\n", language = "tix")
        }
        expect(1) { results.nextIndex }
    }

    @Test
    fun parse_withoutLanguage() {
        val arguments = """
            ```
            code goes here
            *next line*
            ```
        """.trimIndent().toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments) {
            codeBlock(code = "\ncode goes here\n*next line*\n")
        }
        expect(1) { results.nextIndex }
    }
}