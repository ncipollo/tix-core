package org.tix.feature.plan.domain.ticket

data class PlanningContext<R : TicketPlanResult>(
    val level: Int = 0,
    val parentTicket: R? = null,
    val variables: Map<String, String> = emptyMap()
) {
    fun applyResults(results: Map<String, String>) =
        copy(variables = this.variables + results)

    fun createChildContext(parent: R): PlanningContext<R> {
        val filteredVariables = variables.filterKeys { it != "ticket.previous" }
        val ticketVariables = mapOf(
            "ticket.parent" to parent.id
        )
        return PlanningContext(
            level = level + 1,
            parentTicket = parent,
            variables = filteredVariables + ticketVariables
        )
    }

    fun createResultContext(result: R): PlanningContext<R> {
        val ticketVariables = mapOf(
            "ticket.previous" to result.id
        )
        return PlanningContext(
            level = level,
            parentTicket = parentTicket,
            variables = variables + result.results + ticketVariables
        )
    }
}
