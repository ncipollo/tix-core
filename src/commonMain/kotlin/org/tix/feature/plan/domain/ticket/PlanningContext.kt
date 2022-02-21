package org.tix.feature.plan.domain.ticket

import org.tix.config.data.TicketSystemConfiguration
import org.tix.config.data.emptyTicketSystemConfig

data class PlanningContext<R : TicketPlanResult>(
    val config: TicketSystemConfiguration = emptyTicketSystemConfig(),
    val level: Int = 0,
    val parentTicket: R? = null,
    val variables: Map<String, String> = emptyMap()
) {
    fun applyResults(results: Map<String, String>) =
        copy(variables = this.variables + results)

    fun createChildContext(parent: R): PlanningContext<R> {
        val filteredVariables = variables.filterKeys { !it.startsWith("ticket") }
        val ticketVariables = mapOf(
            "ticket.parent.id" to parent.id
        )
        return copy(
            level = level + 1,
            parentTicket = parent,
            variables = filteredVariables + ticketVariables
        )
    }

    fun createResultContext(result: R): PlanningContext<R> {
        val ticketVariables = mapOf(
            "ticket.description" to result.description,
            "ticket.id" to result.id,
            "ticket.previous.description" to result.description,
            "ticket.previous.id" to result.id,
        )
        return copy(variables = variables + result.results + ticketVariables)
    }
}
