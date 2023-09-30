package org.tix.feature.plan.domain.ticket

import org.tix.config.data.MatrixEntryConfiguration
import org.tix.ticket.Ticket

/**
 * Determines which matrix to use with a ticket, if any.
 */
class MatrixFinder(
    private val matrix: Map<String, List<MatrixEntryConfiguration>>,
    private val variableToken: String) {

    fun findMatrixToUse(ticket: Ticket): List<MatrixEntryConfiguration> {
        val matrixVariable = matrix.keys.firstOrNull { containsMatrixVariable(ticket, it) }
        return matrixVariable?.let { matrix[it] } ?: emptyList()
    }

    private fun containsMatrixVariable(ticket: Ticket, matrixVariable: String): Boolean {
        val variableWithToken = "$variableToken$matrixVariable"
        return ticket.title.contains(variableWithToken)
    }
}