package org.tix.feature.plan.parse.nodeparser

import org.tix.model.ticket.body.*
import kotlin.test.Test
import kotlin.test.expect

class BulletItemParserTest {
    private val parser = BulletItemParser(NodeParserMap())

    @Test
    fun parse_levelOneDashBullet() {
        val arguments = "- text*emph*".toParserArguments().childArguments!!
        arguments.state.startTicket()
        arguments.state.listState.buildBulletList {
            val results = parser.parse(arguments)

            val expectedSegment = BulletListItemSegment(
                body = listOf(
                    ParagraphSegment(
                        body = listOf(
                            TextSegment(text = "text"),
                            EmphasisSegment(text = "emph")
                        ).toTicketBody()
                    )
                ).toTicketBody(),
                level = 1,
                marker = "-"
            )
            expect(listOf<BodySegment>(expectedSegment)) { arguments.state.currentTicket!!.body }
            expect(1) { results.nextIndex }
        }
    }

    @Test
    fun parse_levelZeroStarBullet() {
        val arguments = "* text*emph*".toParserArguments().childArguments!!
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        val expectedSegment = BulletListItemSegment(
            body = listOf(
                ParagraphSegment(
                    body = listOf(
                        TextSegment(text = "text"),
                        EmphasisSegment(text = "emph")
                    ).toTicketBody()
                )
            ).toTicketBody(),
            level = 0,
            marker = "*"
        )
        expect(listOf<BodySegment>(expectedSegment)) { arguments.state.currentTicket!!.body }
        expect(1) { results.nextIndex }
    }
}