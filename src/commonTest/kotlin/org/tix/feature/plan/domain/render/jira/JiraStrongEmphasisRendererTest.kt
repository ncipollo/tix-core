package org.tix.feature.plan.domain.render.jira

import org.tix.ticket.body.EmphasisSegment
import org.tix.ticket.body.StrongEmphasisSegment
import kotlin.test.Test
import kotlin.test.expect

class JiraStrongEmphasisRendererTest {
    private val emphasisRenderer = JiraStrongEmphasisRenderer()

    @Test
    fun render() {
        val segment = StrongEmphasisSegment("text")

        val expected = "*text*"
        expect(expected) { emphasisRenderer.render(segment) }
    }
}