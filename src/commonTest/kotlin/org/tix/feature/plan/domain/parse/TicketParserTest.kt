package org.tix.feature.plan.domain.parse

import org.tix.feature.plan.domain.parse.nodeparser.expectBody
import kotlin.test.Test
import kotlin.test.expect

class TicketParserTest {
    private val markdown =
        """
            # Ticket 1
            text
            *emph*
            **strong**
            ---
            - bullet
            - bullet
            
            1. ordered
            2. ordered
           
            > block `span`
            
            ```kotlin
            fun test() = println("hi")
            ```
            
            ```tix
            project: 1
            ```
            
            ()[]!- snake_case
            http://api.example.com
            # Ticket 2
            Body
            ## Nested Ticket
            Body
            # Ticket 3
            Body
            ## Nested Ticket 1
            Body
            ### Deeply Nested Ticket
            ## Nested Ticket 2
        """.trimIndent()
    private val parser = TicketParser()
    private val tickets = parser.parse(markdown)

    @Test
    fun parse_hasExpectedBody() {
        expectBody(tickets[0]) {
            paragraph {
                text("text")
                linebreak()
                emphasis("emph")
                linebreak()
                strongEmphasis("strong")
            }

            linebreak()
            thematicBreak()
            linebreak()

            bulletList {
                bulletListItem { paragraph { text("bullet") } }
                linebreak()
                bulletListItem { paragraph { text("bullet") } }
            }

            repeat(2) { linebreak() }

            orderedList {
                orderedListItem(0, 1) { paragraph { text("ordered") } }
                linebreak()
                orderedListItem(0, 2) { paragraph { text("ordered") } }
            }

            repeat(2) { linebreak() }

            blockQuote {
                paragraph {
                    text("block")
                    whitespace()
                    codeSpan("span")
                }
            }

            repeat(2) { linebreak() }

            codeBlock("\nfun test() = println(\"hi\")\n", "kotlin")

            repeat(2) { linebreak() }

            codeBlock("\nproject: 1\n", "tix")

            repeat(2) { linebreak() }

            paragraph {
                text("(")
                text(")")
                text("[")
                text("]")
                text("!")
                text("-")
                whitespace(1)
                text("snake_case")
                linebreak()
                text("http://api.example.com")
            }

            linebreak()
        }
    }

    @Test
    fun parse_hasExpectedTicketStructure() {
        expect(listOf("Ticket 1", "Ticket 2", "Ticket 3")) { tickets.map { it.title } }
        expect("Nested Ticket") { tickets[1].children[0].title }
        expect("Nested Ticket 1") { tickets[2].children[0].title }
        expect("Deeply Nested Ticket") { tickets[2].children[0].children[0].title }
        expect("Nested Ticket 2") { tickets[2].children[1].title }
    }
}