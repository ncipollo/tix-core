package org.tix.feature.plan.parse.nodeparser

import org.tix.model.ticket.body.*
import kotlin.test.Test
import kotlin.test.expect

class ListParserTest {
    private val parser = ListParser(NodeParserMap())

    @Test
    fun parse_bullet_singleLevel() {
        val arguments = """
            - item1
            - item2
        """.trimIndent().toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        val expectedSegment = bulletList(simpleBullet("item1"), lineBreak(), simpleBullet("item2"))
        expect(listOf<BodySegment>(expectedSegment)) { arguments.state.currentTicket!!.body }
        expect(1) { results.nextIndex }
    }

    private fun bulletList(vararg segments: BodySegment, level: Int = 0) =
        BulletListSegment(body = segments.toList().toTicketBody(), level = level)

    private fun lineBreak() = LinebreakSegment

    private fun simpleBullet(text: String, level: Int = 0) =
        BulletListItemSegment(
            body = listOf(
                ParagraphSegment(
                    body = listOf(
                        TextSegment(text = text),
                    ).toTicketBody()
                )
            ).toTicketBody(),
            level = level,
            marker = "-"
        )
}