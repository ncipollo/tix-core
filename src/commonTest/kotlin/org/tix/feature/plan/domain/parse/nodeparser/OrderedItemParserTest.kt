package org.tix.feature.plan.domain.parse.nodeparser

import kotlin.test.Test
import kotlin.test.expect

class OrderedItemParserTest {
    private val parser = OrderedItemParser(NodeParserMap())

    @Test
    fun parse_numberWithParenthesis() {
        val arguments = "12) text*emph*".toParserArguments().childArguments!!
        arguments.state.startTicket()
        arguments.state.listState.buildBulletList {
            val results = parser.parse(arguments)
            
            expectBody(arguments) {
                orderedListItem(level = 0, number = 12) {
                    paragraph {
                        text("text")
                        emphasis("emph")
                    }
                }
            }
            expect(1) { results.nextIndex }
        }
    }

    @Test
    fun parse_numberWithPeriod() {
        val arguments = "1. text*emph*".toParserArguments().childArguments!!
        arguments.state.startTicket()
        arguments.state.listState.buildBulletList {
            val results = parser.parse(arguments)

            expectBody(arguments) {
                orderedListItem(level = 0, number = 1) {
                    paragraph {
                        text("text")
                        emphasis("emph")
                    }
                }
            }
            expect(1) { results.nextIndex }
        }
    }
}