package org.tix.feature.plan.domain.ticket

import kotlinx.coroutines.flow.*
import org.tix.config.data.TixConfiguration
import org.tix.domain.FlowTransformer
import org.tix.feature.plan.domain.error.TicketPlanningException
import org.tix.ticket.Ticket

class TicketPlannerUseCase(
    private val plannerFactory: TicketPlannerFactory
) : FlowTransformer<TicketPlannerAction, TicketPlanStatus> {
    override fun transformFlow(upstream: Flow<TicketPlannerAction>) =
        upstream.flatMapLatest { action ->
            val planners = plannerFactory.planners(action.shouldDryRun, action.config)
            if (planners.isEmpty()) {
                flowOf(TicketPlanFailed(TicketPlanningException("no ticket systems configs")))
            } else {
                planners.asFlow()
                    .flatMapConcat { it.plan(action.tickets) }
            }
        }
}

data class TicketPlannerAction(
    val config: TixConfiguration,
    val shouldDryRun: Boolean,
    val tickets: List<Ticket>
)