package org.tix.feature.plan.domain.validation

import org.tix.feature.plan.domain.error.ticketPlanningError
import org.tix.feature.plan.domain.stats.LevelLabels
import org.tix.ticket.Ticket

class TicketDepthValidation(
    private val levelLabels: LevelLabels,
): PlanValidation {
    override fun validate(tickets: List<Ticket>) {
        tickets.forEach {
            if(it.maxDepth >= levelLabels.levelCount) {
                ticketPlanningError("Ticket depth of ${it.maxDepth} is not supported")
            }
        }
    }
}