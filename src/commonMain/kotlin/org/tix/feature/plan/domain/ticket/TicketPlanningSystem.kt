package org.tix.feature.plan.domain.ticket

import org.tix.config.data.Workflow
import org.tix.feature.plan.domain.stats.jiraTicketStats
import org.tix.feature.plan.domain.validation.planValidators
import org.tix.ticket.RenderedTicket
import org.tix.ticket.Ticket

interface TicketPlanningSystem<R : TicketPlanResult> {
    suspend fun setup(context: PlanningContext<R>)
    suspend fun planTicket(context: PlanningContext<R>, ticket: RenderedTicket, operation: PlanningOperation): R
    suspend fun executeWorkFlow(workflow: Workflow, context: PlanningContext<*>): Map<String, String>
    suspend fun completeInfo(): PlanningCompleteInfo
    suspend fun validate(context: PlanningContext<R>, tickets: List<Ticket>) {
        val ticketStats = jiraTicketStats(startingLevel = context.config.startingLevel)
        planValidators(ticketStats)
            .forEach { it.validate(tickets) }
    }
}