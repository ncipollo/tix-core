package org.tix.feature.plan.domain.render.github

import org.tix.model.ticket.body.CodeBlockSegment
import kotlin.test.Test
import kotlin.test.expect

class GithubCodeBlockRendererTest {
    @Test
    fun render_withLanguage() {
        val segment = CodeBlockSegment("println(\"hi\")", "kotlin")

        val expected = """
            ```kotlin
            println("hi")
            ```
        """.trimIndent()
        expect(expected) { GithubCodeBlockRenderer().render(segment) }
    }

    @Test
    fun render_withoutLanguage() {
        val segment = CodeBlockSegment("println(\"hi\")")

        val expected = """
            ```
            println("hi")
            ```
        """.trimIndent()
        expect(expected) { GithubCodeBlockRenderer().render(segment) }
    }
}