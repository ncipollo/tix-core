package org.tix.feature.plan.domain.render.jira

import org.tix.model.ticket.body.LinkSegment
import kotlin.test.Test
import kotlin.test.expect

class JiraLinkRendererTest {
    private val codeSpanRenderer = JiraLinkRenderer()

    @Test
    fun render() {
        val segment = LinkSegment("https://api.example.com", "url")

        val expected = "[url|https://api.example.com]"
        expect(expected) { codeSpanRenderer.render(segment) }
    }
}