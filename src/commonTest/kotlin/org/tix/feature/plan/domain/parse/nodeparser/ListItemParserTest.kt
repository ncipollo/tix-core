package org.tix.feature.plan.domain.parse.nodeparser

import kotlin.test.Test
import kotlin.test.expect

class ListItemParserTest {
    private val parser = ListItemParser(NodeParserMap())

    @Test
    fun parse_bulletedItem() {
        val arguments = "- text".toParserArguments().childArguments!!
        arguments.state.startTicket()
        arguments.state.listState.buildBulletList {
            val results = parser.parse(arguments)

            expectBody(arguments) {
                bulletListItem(marker = "-") {
                    paragraph {
                        text("text")
                    }
                }
            }
            expect(1) { results.nextIndex }
        }
    }

    @Test
    fun parse_orderedItem() {
        val arguments = "1. text".toParserArguments().childArguments!!
        arguments.state.startTicket()
        arguments.state.listState.buildOrderedList {
            val results = parser.parse(arguments)

            expectBody(arguments) {
                orderedListItem(number = 1) {
                    paragraph {
                        text("text")
                    }
                }
            }
            expect(1) { results.nextIndex }
        }
    }
}