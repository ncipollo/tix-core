package org.tix.feature.plan.domain.render.common

import org.tix.feature.plan.domain.parse.TicketParser
import org.tix.feature.plan.domain.parse.TicketParserArguments
import org.tix.feature.plan.domain.render.github.githubBodyRenderer
import org.tix.feature.plan.domain.render.jira.jiraBodyRenderer
import org.tix.fixture.config.tixConfiguration
import kotlin.test.Test
import kotlin.test.expect

class BulletListRendererTest {
    private val markdown = """
        # Ticket 1
        * Item 1
        * Item 2
            * Child 1
            line 2
            * Child 2
    """.trimIndent()
    private val parser = TicketParser()
    private val args = TicketParserArguments(markdown = markdown, configuration = tixConfiguration)
    private val tickets = parser.parse(args)

    @Test
    fun render_github() {
        val bodyRenderer = githubBodyRenderer()
        val expected = """
            |* Item 1
            |* Item 2
            |    * Child 1
            |    line 2
            |    * Child 2
        """.trimMargin()

        expect(expected) { bodyRenderer.render(tickets[0].body) }
    }

    @Test
    fun render_jira() {
        val bodyRenderer = jiraBodyRenderer()
        val expected = """
            |* Item 1
            |* Item 2
            |** Child 1
            |    line 2
            |** Child 2
        """.trimMargin()

        expect(expected) { bodyRenderer.render(tickets[0].body) }
    }
}