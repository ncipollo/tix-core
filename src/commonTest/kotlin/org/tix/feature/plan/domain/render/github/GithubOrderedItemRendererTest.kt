package org.tix.feature.plan.domain.render.github

import org.tix.ticket.body.LinebreakSegment
import org.tix.ticket.body.OrderedListItemSegment
import org.tix.ticket.body.TextSegment
import org.tix.ticket.body.TicketBody
import kotlin.test.Test
import kotlin.test.expect

class GithubOrderedItemRendererTest {
    private val bodyRenderer = githubBodyRenderer()
    private val orderedItemRenderer = GithubOrderedItemRenderer(bodyRenderer)

    @Test
    fun render_whenLevelIs0_rendersItemsWithoutIndent() {
        val quoteBody = TicketBody(
            listOf(
                TextSegment("text1"),
                LinebreakSegment,
                TextSegment("text2")
            )
        )
        val segment = OrderedListItemSegment(quoteBody)

        val expected = """
            1. text1
            text2
        """.trimIndent()
        expect(expected) { orderedItemRenderer.render(segment) }
    }

    @Test
    fun render_whenLevelIsGreaterThan0_withASingleLine_rendersItemsWithIndent() {
        val quoteBody = TicketBody(
            listOf(TextSegment("text1"))
        )
        val segment = OrderedListItemSegment(body = quoteBody, level = 2)

        val expected = "        1. text1"
        expect(expected) { orderedItemRenderer.render(segment) }
    }

    @Test
    fun render_whenLevelIsGreaterThan0_withMultipleLines_rendersItemsWithIndent() {
        val quoteBody = TicketBody(
            listOf(
                TextSegment("text1"),
                LinebreakSegment,
                TextSegment("text2")
            )
        )
        val segment = OrderedListItemSegment(body = quoteBody, level = 2)

        val expected = """
            |        1. text1
            |text2
        """.trimMargin()
        expect(expected) { orderedItemRenderer.render(segment) }
    }
}