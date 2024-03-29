package org.tix.feature.plan.domain.render.jira

import org.tix.ticket.body.CodeBlockSegment
import kotlin.test.Test
import kotlin.test.expect

class JiraCodeBlockRendererTest {
    @Test
    fun render_withLanguage() {
        val segment = CodeBlockSegment("\nprintln(\"hi\")\n", "kotlin")

        val expected = """
            {code:kotlin}
            println("hi")
            {code}
        """.trimIndent()
        expect(expected) { JiraCodeBlockRenderer().render(segment) }
    }

    @Test
    fun render_withoutLanguage() {
        val segment = CodeBlockSegment("\nprintln(\"hi\")\n")

        val expected = """
            {code}
            println("hi")
            {code}
        """.trimIndent()
        expect(expected) { JiraCodeBlockRenderer().render(segment) }
    }
}