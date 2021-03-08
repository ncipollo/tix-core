package org.tix.feature.plan.parse.nodeparser

import org.tix.model.ticket.body.*
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

            val expectedSegment = OrderedListItemSegment(
                body = listOf(
                    ParagraphSegment(
                        body = listOf(
                            TextSegment(text = "text"),
                            EmphasisSegment(text = "emph")
                        ).toTicketBody()
                    )
                ).toTicketBody(),
                level = 1,
                number = 12
            )
            expect(listOf<BodySegment>(expectedSegment)) { arguments.state.currentTicket!!.body }
            expect(1) { results.nextIndex }
        }
    }

    @Test
    fun parse_numberWithPeriod() {
        val arguments = "1. text*emph*".toParserArguments().childArguments!!
        arguments.state.startTicket()
        arguments.state.listState.buildBulletList {
            val results = parser.parse(arguments)

            val expectedSegment = OrderedListItemSegment(
                body = listOf(
                    ParagraphSegment(
                        body = listOf(
                            TextSegment(text = "text"),
                            EmphasisSegment(text = "emph")
                        ).toTicketBody()
                    )
                ).toTicketBody(),
                level = 1,
                number = 1
            )
            expect(listOf<BodySegment>(expectedSegment)) { arguments.state.currentTicket!!.body }
            expect(1) { results.nextIndex }
        }
    }
}