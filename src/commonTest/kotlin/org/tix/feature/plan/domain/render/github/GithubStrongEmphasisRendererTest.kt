package org.tix.feature.plan.domain.render.github

import org.tix.ticket.body.StrongEmphasisSegment
import kotlin.test.Test
import kotlin.test.expect

class GithubStrongEmphasisRendererTest {
    private val emphasisRenderer = GithubStrongEmphasisRenderer()

    @Test
    fun render() {
        val segment = StrongEmphasisSegment("text")

        val expected = "**text**"
        expect(expected) { emphasisRenderer.render(segment) }
    }
}