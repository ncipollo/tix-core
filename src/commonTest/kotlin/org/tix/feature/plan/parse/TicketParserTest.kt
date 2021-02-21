package org.tix.feature.plan.parse

import kotlin.test.Test

class TicketParserTest {
    private val markdown =
        """
            # Section 1
            Ticket 1
            # Section 2
            Ticket 2
            ## Section 3
            Sub Ticket 1
            - Item 1 blah blah
            blah blah blah
            - Item 2
            - Item 3
            ```tix
            epic: test
            ```
        """.trimIndent()
    private val parser = TicketParser()

    @Test
    fun parse() {
        try {
            parser.parse(markdown)
        } catch (e: ParseException) {

        }
    }
}