package org.tix.feature.plan.domain.ticket.dry

import org.tix.feature.plan.domain.ticket.PlanningOperation
import kotlin.test.Test
import kotlin.test.expect

class DryRunTicketPlanResultTest {
    private val result = DryRunTicketPlanResult(
        id = "1",
        title = "Title",
        ticketType = "Epic",
        body = "Line 1\nLine 2",
        fields = mapOf(
            "bool" to true,
            "float" to 3.14f,
            "int" to 3,
            "map" to mapOf("foo" to "bar"),
            "null" to null,
            "string" to "string",
        ),
        operation = PlanningOperation.CreateTicket
    )

    @Test
    fun description_create() {
        val expected = """
            -----------------
            Title
            Line 1
            Line 2
            ```tix_info
            Fields:
            - bool = true
            - float = 3.14
            - int = 3
            - map = {foo=bar}
            - null = null
            - string = string
            Ticket Type: Epic
            Operation: create
            ```
            
        """.trimIndent()

        expect(expected) {
            result.description
        }
    }

    @Test
    fun description_delete() {
        val expected = """
            -----------------
            Title
            Line 1
            Line 2
            ```tix_info
            Fields:
            - bool = true
            - float = 3.14
            - int = 3
            - map = {foo=bar}
            - null = null
            - string = string
            Ticket Type: Epic
            Operation: delete id
            ```
            
        """.trimIndent()

        expect(expected) {
            result.copy(operation = PlanningOperation.DeleteTicket("id")).description
        }
    }

    @Test
    fun description_update() {
        val expected = """
            -----------------
            Title
            Line 1
            Line 2
            ```tix_info
            Fields:
            - bool = true
            - float = 3.14
            - int = 3
            - map = {foo=bar}
            - null = null
            - string = string
            Ticket Type: Epic
            Operation: update id
            ```
            
        """.trimIndent()

        expect(expected) {
            result.copy(operation = PlanningOperation.UpdateTicket("id")).description
        }
    }

    @Test
    fun wasDryRun() {
        expect(true) { result.wasDryRun }
    }
}