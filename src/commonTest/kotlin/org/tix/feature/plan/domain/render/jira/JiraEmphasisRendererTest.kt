package org.tix.feature.plan.domain.render.jira

import org.tix.model.ticket.body.EmphasisSegment
import kotlin.test.Test
import kotlin.test.expect

class JiraEmphasisRendererTest {
    private val codeSpanRenderer = JiraEmphasisRenderer()

    @Test
    fun render() {
        val segment = EmphasisSegment("text")

        val expected = "_text_"
        expect(expected) { codeSpanRenderer.render(segment) }
    }
}