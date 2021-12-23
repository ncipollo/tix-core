package org.tix.feature.plan.domain.render.jira

import org.tix.ticket.body.LinebreakSegment
import org.tix.ticket.body.OrderedListItemSegment
import org.tix.ticket.body.TextSegment
import org.tix.ticket.body.TicketBody
import kotlin.test.Test
import kotlin.test.expect

class JiraOrderedItemRendererTest {
    private val bodyRenderer = jiraBodyRenderer()
    private val orderedItemRenderer = JiraOrderedItemRenderer(bodyRenderer)

    @Test
    fun render_whenLevelIs0_rendersBulletWithoutIndent() {
        val quoteBody = TicketBody(
            listOf(
                TextSegment("text1"),
                LinebreakSegment,
                TextSegment("text2")
            )
        )
        val segment = OrderedListItemSegment(quoteBody)

        val expected = """
            # text1
            text2
        """.trimIndent()
        expect(expected) { orderedItemRenderer.render(segment) }
    }

    @Test
    fun render_whenLevelIsGreaterThan0_withASingleLine_rendersBulletWithoutIndent() {
        val quoteBody = TicketBody(
            listOf(TextSegment("text1"))
        )
        val segment = OrderedListItemSegment(body = quoteBody, level = 2)

        val expected = "### text1"
        expect(expected) { orderedItemRenderer.render(segment) }
    }

    @Test
    fun render_whenLevelIsGreaterThan0_withMultipleLines_rendersBulletWithoutIndent() {
        val quoteBody = TicketBody(
            listOf(
                TextSegment("text1"),
                LinebreakSegment,
                TextSegment("text2")
            )
        )
        val segment = OrderedListItemSegment(body = quoteBody, level = 2)

        val expected = """
            |### text1
            |text2
        """.trimMargin()
        expect(expected) { orderedItemRenderer.render(segment) }
    }
}