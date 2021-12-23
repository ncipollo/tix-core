package org.tix.feature.plan.domain.render.github

import org.tix.ticket.body.CodeSpanSegment
import kotlin.test.Test
import kotlin.test.expect

class GithubCodeSpanRendererTest {
    private val codeSpanRenderer = GithubCodeSpanRenderer()

    @Test
    fun render() {
        val segment = CodeSpanSegment("code")

        val expected = "`code`"
        expect(expected) { codeSpanRenderer.render(segment) }
    }
}