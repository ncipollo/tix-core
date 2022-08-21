package org.tix.feature.plan.domain.ticket

import org.tix.config.data.TixConfiguration
import org.tix.ticket.Ticket

class TicketPlannerUseCase {
}

data class TicketPlannerAction(
    val config: TixConfiguration,
    val shouldDryRun: Boolean,
    val tickets: List<Ticket>
)