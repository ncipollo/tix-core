package org.tix.feature.plan.domain.ticket

import org.tix.config.data.MatrixEntryConfiguration
import org.tix.serialize.dynamic.DynamicElement
import org.tix.ticket.Ticket
import kotlin.test.Test
import kotlin.test.expect

class MatrixPlannerTest {
    private companion object {
        const val TOKEN = "$"
    }

    private val matrix = mapOf(
        "matrix_a" to listOf(
            MatrixEntryConfiguration(mapOf("matrix_a" to "value_1")),
            MatrixEntryConfiguration(mapOf("matrix_a" to "value_2"))
        ),
        "matrix_b" to listOf(
            MatrixEntryConfiguration(mapOf("matrix_b" to "value_3")),
            MatrixEntryConfiguration(mapOf("matrix_b" to "value_4"))
        )
    )
    private val tickets = listOf(
        Ticket(title = "\$matrix_a: title", fields = DynamicElement("fields"), tixId = "tix_a"),
        Ticket(title = "\$matrix_b: title", tixId = "tix_b"),
        Ticket(title = "no_matrix", tixId = "tix_c"),
    )

    @Test
    fun matrixPlan_emptyMap() {
        val matrixPlanner = MatrixPlanner(emptyMap(), TOKEN)
        expect(tickets) { matrixPlanner.matrixPlan(tickets) }
    }

    @Test
    fun matrixPlan_ticketsUseMatrix() {
        val matrixPlanner = MatrixPlanner(matrix, TOKEN)
        val expectedTickets = listOf(
            Ticket(
                title = "\$matrix_a: title",
                fields = DynamicElement("fields"),
                variables = mapOf("matrix_a" to "value_1"),
                tixId = "tix_a_0"
            ),
            Ticket(
                title = "\$matrix_a: title",
                fields = DynamicElement("fields"),
                variables = mapOf("matrix_a" to "value_2"),
                tixId = "tix_a_1"
            ),
            Ticket(
                title = "\$matrix_b: title",
                variables = mapOf("matrix_b" to "value_3"),
                tixId = "tix_b_0"
            ),
            Ticket(
                title = "\$matrix_b: title",
                variables = mapOf("matrix_b" to "value_4"),
                tixId = "tix_b_1"
            ),
            Ticket(title = "no_matrix", tixId = "tix_c"),
        )
        expect(expectedTickets) { matrixPlanner.matrixPlan(tickets) }
    }
}