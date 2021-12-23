package org.tix.feature.plan.domain.render.jira

import org.tix.ticket.body.*
import kotlin.test.Test
import kotlin.test.expect

class JiraBlockQuoteRendererTest {
    private val bodyRenderer = jiraBodyRenderer()
    private val blockQuoteRenderer = JiraBlockQuoteRenderer(bodyRenderer)

    @Test
    fun render_whenBodyIsEmpty() {
        val segment = BlockQuoteSegment(emptyBody())
        expect("") { blockQuoteRenderer.render(segment) }
    }

    @Test
    fun render_whenBodyIsNotEmpty() {
        val quoteBody = TicketBody(
            listOf(
                TextSegment("text1"),
                LinebreakSegment,
                TextSegment("text2")
            )
        )
        val segment = BlockQuoteSegment(quoteBody)

        val expected = """
            {quote}
            text1
            text2
            {quote}
        """.trimIndent()
        expect(expected) { blockQuoteRenderer.render(segment) }
    }
}