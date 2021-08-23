package org.tix.feature.plan.domain.parse.nodeparser

import org.tix.feature.plan.domain.parse.ParseException
import org.tix.serialize.dynamic.DynamicElement
import org.tix.serialize.dynamic.emptyDynamic
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.expect

class CodeFenceParserTest {
    private val parser = CodeFenceParser()

    @Test
    fun parse_withLanguage() {
        val arguments = """
            ```code
            code goes here
            *next line*
            ```
        """.trimIndent().toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)
        expectBody(arguments) {
            codeBlock(code = "\ncode goes here\n*next line*\n", language = "code")
        }
        expect(1) { results.nextIndex }
        expect(emptyDynamic()) { arguments.state.currentTicket!!.fields }
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
        expect(emptyDynamic()) { arguments.state.currentTicket!!.fields }
    }

    @Test
    fun parse_withTix_addsFieldsWhenFormatIsValid() {
        val arguments = """
            ```tix
            field1: value1
            field2: value2
            ```
        """.trimIndent().toParserArguments()
        arguments.state.startTicket()

        parser.parse(arguments)

        val expectedMap = mapOf("field1" to "value1", "field2" to "value2")
        val expected = DynamicElement(expectedMap)
        expect(expected) { arguments.state.currentTicket!!.fields }
        // There should be no segment added to the body
        expect(emptyList()) { arguments.state.currentTicket!!.body }
    }

    @Test
    fun parse_withTix_throwsWhenFormatIsInvalid() {
        val arguments = """
            ```tix_json
            field1: value1
            ```
        """.trimIndent().toParserArguments()
        arguments.state.startTicket()

        assertFailsWith<ParseException> { parser.parse(arguments) }
    }
}