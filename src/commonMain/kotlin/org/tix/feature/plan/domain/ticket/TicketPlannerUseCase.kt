package org.tix.feature.plan.domain.ticket

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import org.tix.config.data.TixConfiguration
import org.tix.domain.FlowTransformer
import org.tix.ticket.Ticket

class TicketPlannerUseCase(
    private val plannerFactory: TicketPlannerFactory
) : FlowTransformer<TicketPlannerAction, TicketPlanStatus> {
    override fun transformFlow(upstream: Flow<TicketPlannerAction>) =
        upstream.flatMapLatest { action ->
            val planners = plannerFactory.planners(action.shouldDryRun, action.config)
            planners.asFlow()
                .flatMapConcat { it.plan(action.tickets) }
        }
}

data class TicketPlannerAction(
    val config: TixConfiguration,
    val shouldDryRun: Boolean,
    val tickets: List<Ticket>
)