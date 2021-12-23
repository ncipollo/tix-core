package org.tix.feature.plan.domain.ticket

import org.tix.config.data.Workflow
import org.tix.ticket.Ticket

interface TicketPlanningSystem<R : TicketPlanResult> {
    suspend fun setup()
    suspend fun planTicket(context: PlanningContext<R>, ticket: Ticket): R
    suspend fun executeWorkFlow(workflow: Workflow, context: PlanningContext<*>): Map<String, String>
    suspend fun completeInfo(): PlanningCompleteInfo
    suspend fun validate(tickets: List<Ticket>)
}