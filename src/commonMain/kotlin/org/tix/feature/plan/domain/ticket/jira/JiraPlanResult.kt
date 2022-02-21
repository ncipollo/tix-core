package org.tix.feature.plan.domain.ticket.jira

import org.tix.feature.plan.domain.ticket.PlanningOperation
import org.tix.feature.plan.domain.ticket.TicketPlanResult

data class JiraPlanResult(
    override val id: String = "",
    override val key: String = "",
    override val level: Int = 0,
    override val description: String = "",
    override val results: Map<String, String> = emptyMap(),
    override val operation: PlanningOperation = PlanningOperation.CreateTicket
) : TicketPlanResult