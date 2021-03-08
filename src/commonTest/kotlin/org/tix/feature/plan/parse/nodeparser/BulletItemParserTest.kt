package org.tix.feature.plan.parse.nodeparser

import kotlin.test.Test
import kotlin.test.expect

class BulletItemParserTest {
    private val parser = BulletItemParser(NodeParserMap())

    @Test
    fun parse_dashBullet() {
        val arguments = "- text*emph*".toParserArguments().childArguments!!
        arguments.state.startTicket()
        arguments.state.listState.buildBulletList {
            val results = parser.parse(arguments)

            expectBody(arguments) {
                bulletListItem(level = 0, marker = "-") {
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
    fun parse_starBullet() {
        val arguments = "* text*emph*".toParserArguments().childArguments!!
        arguments.state.startTicket()

        arguments.state.listState.buildBulletList {
            val results = parser.parse(arguments)

            expectBody(arguments) {
                bulletListItem(level = 0, marker = "*") {
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