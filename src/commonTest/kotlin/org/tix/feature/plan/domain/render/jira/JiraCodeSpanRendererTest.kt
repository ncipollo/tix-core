package org.tix.feature.plan.domain.render.jira

import org.tix.ticket.body.CodeSpanSegment
import kotlin.test.Test
import kotlin.test.expect

class JiraCodeSpanRendererTest {
    private val codeSpanRenderer = JiraCodeSpanRenderer()

    @Test
    fun render() {
        val segment = CodeSpanSegment("code")

        val expected = "{{code}}"
        expect(expected) { codeSpanRenderer.render(segment) }
    }
}