package org.tix.feature.plan.domain.ticket.dry

import org.tix.config.data.Workflow
import org.tix.feature.plan.domain.render.BodyRenderer
import org.tix.feature.plan.domain.stats.TicketStats
import org.tix.feature.plan.domain.ticket.PlanningCompleteInfo
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.TicketPlanningSystem
import org.tix.feature.plan.domain.validation.planValidators
import org.tix.ticket.Ticket

class DryRunPlanningSystem(
    private val renderer: BodyRenderer,
    private val ticketStats: TicketStats
) : TicketPlanningSystem<DryRunTicketPlanResult> {

    override suspend fun setup() = Unit

    override suspend fun planTicket(
        context: PlanningContext<DryRunTicketPlanResult>,
        ticket: Ticket
    ): DryRunTicketPlanResult {
        ticketStats.countTicket(context.level)

        return DryRunTicketPlanResult(
            level = context.level,
            title = ticket.title,
            ticketType = ticketStats.capitalizedLabel(context.level, 1),
            body = renderer.render(ticket.body)
        )
    }

    override suspend fun executeWorkFlow(
        workflow: Workflow,
        context: PlanningContext<*>
    ): Map<String, String> = emptyMap()

    override suspend fun completeInfo(): PlanningCompleteInfo = PlanningCompleteInfo(message = ticketStats.render())

    override suspend fun validate(tickets: List<Ticket>) {
        planValidators(ticketStats)
            .forEach { it.validate(tickets) }
    }
}