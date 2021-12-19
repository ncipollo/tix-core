package org.tix.feature.plan.domain.render

import org.tix.feature.plan.domain.parse.TicketParser
import org.tix.feature.plan.domain.parse.TicketParserArguments
import org.tix.feature.plan.domain.render.github.githubBodyRenderer
import org.tix.feature.plan.domain.render.jira.jiraBodyRenderer
import org.tix.fixture.config.tixConfiguration
import kotlin.test.Test
import kotlin.test.expect

class RendererIntegrationTest {
    private val markdown = """
        # Ticket 1
        This is some normal test.
        It's multiple lines.
        And has *emphasized* and **strong** text
        - Bullet 1
        - Bullet 2
        Text after
        ---
        1. Item 1
        2. Item 2
        3. **Strong** Item 3
            - Child 1
        
        Here's a link [Example](https://example.com).
        Here's a code span `code`.
        ```tix
        - Tix block
        ```
        ```kotlin
        println("The above tix block should be omitted")
        ```
            codeBlock1()
            codeBlock2()
        > Never more
    """.trimIndent()
    private val parser = TicketParser()
    private val args = TicketParserArguments(markdown = markdown, configuration = tixConfiguration)
    private val tickets = parser.parse(args)

    @Test
    fun render_github() {
        val renderer = githubBodyRenderer()
        val expected = """
            This is some normal test.
            It's multiple lines.
            And has *emphasized* and **strong** text
            - Bullet 1
            - Bullet 2
            Text after
            ----
            1. Item 1
            1. Item 2
            1. **Strong** Item 3
                - Child 1
            
            Here's a link [Example](https://example.com).
            Here's a code span `code`.
            
            ```kotlin
            println("The above tix block should be omitted")
            ```
            ```
            codeBlock1()
            codeBlock2()
            ```
            > Never more
        """.trimIndent()

        expect(expected) { renderer.render(tickets[0].body) }
    }

    @Test
    fun render_jira() {
        val renderer = jiraBodyRenderer()
        val expected = """
            This is some normal test.
            It's multiple lines.
            And has _emphasized_ and *strong* text
            - Bullet 1
            - Bullet 2
            Text after
            ----
            # Item 1
            # Item 2
            # *Strong* Item 3
            -- Child 1
            
            Here's a link [Example|https://example.com].
            Here's a code span {{code}}.
            
            {code:kotlin}
            println("The above tix block should be omitted")
            {code}
            {code}
            codeBlock1()
            codeBlock2()
            {code}
            {quote}
            Never more
            {quote}
        """.trimIndent()

        expect(expected) { renderer.render(tickets[0].body) }
    }
}