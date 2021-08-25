package org.tix.feature.plan.domain.render.github

import org.tix.model.ticket.body.EmphasisSegment
import kotlin.test.Test
import kotlin.test.expect

class GithubEmphasisRendererTest {
    private val codeSpanRenderer = GithubEmphasisRenderer()

    @Test
    fun render() {
        val segment = EmphasisSegment("text")

        val expected = "*text*"
        expect(expected) { codeSpanRenderer.render(segment) }
    }
}