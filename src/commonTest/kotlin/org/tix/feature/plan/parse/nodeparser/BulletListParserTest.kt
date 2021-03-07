package org.tix.feature.plan.parse.nodeparser

import org.tix.model.ticket.body.BodySegment
import org.tix.model.ticket.body.CodeBlockSegment
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.expect

class BulletListParserTest {
    private val parser = CodeBlockParser()

    @Ignore
    @Test
    fun parse_withLanguage() {
        val arguments = """
            - bullet *emph*
            - bullet
                - bullet
                    1. Number
        """.trimIndent().toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        val expectedSegment = CodeBlockSegment(code = "    line1\n    line2")
        expect(listOf<BodySegment>(expectedSegment)) { arguments.state.currentTicket!!.body }
        expect(1) { results.nextIndex }
    }
}