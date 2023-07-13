package org.tix.feature.plan.domain.ticket.github

import org.tix.feature.plan.domain.ticket.PlanningOperation
import org.tix.feature.plan.domain.ticket.TicketPlanResult

data class GithubPlanResult(
    override val id: String = "",
    override val key: String = "",
    override val tixId: String = "",
    override val level: Int = 0,
    override val description: String = "",
    override val results: Map<String, String> = emptyMap(),
    override val operation: PlanningOperation = PlanningOperation.CreateTicket,
    override val startingLevel: Int = 0
) : TicketPlanResult