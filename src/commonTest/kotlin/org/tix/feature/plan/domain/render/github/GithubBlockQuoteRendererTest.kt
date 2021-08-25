package org.tix.feature.plan.domain.render.github

import org.tix.model.ticket.body.*
import kotlin.test.Test
import kotlin.test.expect

class GithubBlockQuoteRendererTest {
    private val bodyRenderer = githubBodyRenderer()
    private val blockQuoteRenderer = GithubBlockQuoteRenderer(bodyRenderer)

    @Test
    fun render_whenBodyHasMultipleLines() {
        val quoteBody = TicketBody(
            listOf(
                TextSegment("text1"),
                LinebreakSegment,
                TextSegment("text2")
            )
        )
        val segment = BlockQuoteSegment(quoteBody)

        val expected = """
            > text1
            > text2
        """.trimIndent()
        expect(expected) { blockQuoteRenderer.render(segment) }
    }

    @Test
    fun render_whenBodyHasSingleLine() {
        val quoteBody = TicketBody(
            listOf(
                TextSegment("text1"),
            )
        )
        val segment = BlockQuoteSegment(quoteBody)

        val expected = """
            > text1
        """.trimIndent()
        expect(expected) { blockQuoteRenderer.render(segment) }
    }

    @Test
    fun render_whenBodyIsEmpty() {
        val segment = BlockQuoteSegment(emptyBody())
        expect("") { blockQuoteRenderer.render(segment) }
    }
}