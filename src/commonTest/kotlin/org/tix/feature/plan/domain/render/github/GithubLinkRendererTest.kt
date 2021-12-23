package org.tix.feature.plan.domain.render.github

import org.tix.ticket.body.LinkSegment
import kotlin.test.Test
import kotlin.test.expect

class GithubLinkRendererTest {
    private val codeSpanRenderer = GithubLinkRenderer()

    @Test
    fun render() {
        val segment = LinkSegment("https://api.example.com", "url")

        val expected = "[url](https://api.example.com)"
        expect(expected) { codeSpanRenderer.render(segment) }
    }
}