package org.tix.feature.plan.parse.nodeparser

import org.tix.model.ticket.body.*
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

            val expectedSegment = BulletListItemSegment(
                body = listOf(
                    ParagraphSegment(
                        body = listOf(
                            TextSegment(text = "text"),
                        ).toTicketBody()
                    )
                ).toTicketBody(),
                level = 0,
                marker = "-"
            )
            expect(listOf<BodySegment>(expectedSegment)) { arguments.state.currentTicket!!.body }
            expect(1) { results.nextIndex }
        }
    }

    @Test
    fun parse_orderedItem() {
        val arguments = "1. text".toParserArguments().childArguments!!
        arguments.state.startTicket()
        arguments.state.listState.buildOrderedList {
            val results = parser.parse(arguments)

            val expectedSegment = OrderedListItemSegment(
                body = listOf(
                    ParagraphSegment(
                        body = listOf(
                            TextSegment(text = "text"),
                        ).toTicketBody()
                    )
                ).toTicketBody(),
                level = 0,
                number = 1
            )
            expect(listOf<BodySegment>(expectedSegment)) { arguments.state.currentTicket!!.body }
            expect(1) { results.nextIndex }
        }
    }
}