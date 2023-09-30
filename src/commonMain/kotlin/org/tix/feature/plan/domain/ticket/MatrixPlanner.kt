package org.tix.feature.plan.domain.ticket

import org.tix.config.data.MatrixEntryConfiguration
import org.tix.ticket.Ticket

class MatrixPlanner(
    private val matrix: Map<String, List<MatrixEntryConfiguration>>,
    variableToken: String
) {
    private val matrixFinder = MatrixFinder(matrix, variableToken)


    fun matrixPlan(tickets: List<Ticket>): List<Ticket> {
        if (matrix.isEmpty()) {
            return tickets
        }

        return tickets.flatMap {
            val matrixEntries = matrixFinder.findMatrixToUse(it)
            matrixTicket(it, matrixEntries)
        }
    }

    private fun matrixTicket(ticket: Ticket, matrixEntries: List<MatrixEntryConfiguration>) =
        if (matrixEntries.isNotEmpty()) {
            matrixEntries.mapIndexed { index, config ->
                ticket.copy(
                    tixId = "${ticket.tixId}_$index",
                    variables = ticket.variables + config.variables
                )
            }
        } else {
            listOf(ticket)
        }
}