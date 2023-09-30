package org.tix.feature.plan.domain.ticket

import org.tix.config.data.MatrixEntryConfiguration
import org.tix.ticket.Ticket
import kotlin.test.Test
import kotlin.test.expect

class MatrixFinderTest {
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

    @Test
    fun findMatrixToUse_emptyMap() {
        val finder = MatrixFinder(emptyMap(), TOKEN)
        expect(emptyList()) { finder.findMatrixToUse(Ticket(title = "\$matrix_a")) }
    }

    @Test
    fun findMatrixToUse_ticketHasNoMatrix() {
        val finder = MatrixFinder(matrix, TOKEN)
        expect(emptyList()) { finder.findMatrixToUse(Ticket(title = "hi there")) }
    }

    @Test
    fun findMatrixToUse_ticketHasMatrix() {
        val finder = MatrixFinder(matrix, TOKEN)
        expect(matrix["matrix_a"]) { finder.findMatrixToUse(Ticket(title = "\$matrix_a: title")) }
    }
}