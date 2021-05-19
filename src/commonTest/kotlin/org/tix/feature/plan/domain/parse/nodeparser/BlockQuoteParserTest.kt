package org.tix.feature.plan.domain.parse.nodeparser

import kotlin.test.Test
import kotlin.test.expect

class BlockQuoteParserTest {
    private val parser = BlockQuoteParser(NodeParserMap())

    @Test
    fun parse() {
        val arguments = "> text\n*emph* **strong**".toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments) {
            blockQuote {
                paragraph {
                    text("text")
                    linebreak()
                    emphasis("emph")
                    whitespace()
                    strongEmphasis("strong")
                }
            }
        }
        expect(1) { results.nextIndex }
    }
}