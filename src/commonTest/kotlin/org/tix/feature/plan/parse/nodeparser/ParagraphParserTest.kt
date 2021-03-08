package org.tix.feature.plan.parse.nodeparser

import org.tix.model.ticket.body.*
import kotlin.test.Test
import kotlin.test.expect

class ParagraphParserTest {
    private val parser = ParagraphParser(NodeParserMap())

    @Test
    fun parse() {
        val arguments = "text\n*emph* **strong**".toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        val expectedSegment = ParagraphSegment(
            listOf(
                TextSegment("text"),
                LinebreakSegment,
                EmphasisSegment("emph"),
                WhitespaceSegment(1),
                StrongEmphasisSegment("strong"),
            ).toTicketBody()
        )
        expectBody(arguments) {
            paragraph {
                text("text")
                linebreak()
                emphasis("emph")
                whitespace()
                strongEmphasis("strong")
            }
        }
        expect(1) { results.nextIndex }
    }
}