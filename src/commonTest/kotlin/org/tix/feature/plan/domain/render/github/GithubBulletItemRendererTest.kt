package org.tix.feature.plan.domain.render.github

import org.tix.ticket.body.BulletListItemSegment
import org.tix.ticket.body.LinebreakSegment
import org.tix.ticket.body.TextSegment
import org.tix.ticket.body.TicketBody
import kotlin.test.Test
import kotlin.test.expect

class GithubBulletItemRendererTest {
    private val bodyRenderer = githubBodyRenderer()
    private val bulletItemRenderer = GithubBulletItemRenderer(bodyRenderer)

    @Test
    fun render_whenLevelIs0_rendersBulletWithoutIndent() {
        val quoteBody = TicketBody(
            listOf(
                TextSegment("text1"),
                LinebreakSegment,
                TextSegment("text2")
            )
        )
        val segment = BulletListItemSegment(quoteBody)

        val expected = """
            - text1
            text2
        """.trimIndent()
        expect(expected) { bulletItemRenderer.render(segment) }
    }

    @Test
    fun render_whenLevelIsGreaterThan0_withASingleLine_rendersBulletWithIndent() {
        val quoteBody = TicketBody(
            listOf(TextSegment("text1"))
        )
        val segment = BulletListItemSegment(body = quoteBody, level = 2, marker = "*")

        val expected = "        * text1"
        expect(expected) { bulletItemRenderer.render(segment) }
    }

    @Test
    fun render_whenLevelIsGreaterThan0_withMultipleLines_rendersBulletWithIndent() {
        val quoteBody = TicketBody(
            listOf(
                TextSegment("text1"),
                LinebreakSegment,
                TextSegment("text2")
            )
        )
        val segment = BulletListItemSegment(body = quoteBody, level = 2)

        val expected = """
            |        - text1
            |text2
        """.trimMargin()
        expect(expected) { bulletItemRenderer.render(segment) }
    }
}