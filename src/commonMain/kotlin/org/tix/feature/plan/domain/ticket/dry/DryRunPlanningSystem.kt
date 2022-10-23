package org.tix.feature.plan.domain.ticket.dry

import org.tix.config.data.Workflow
import org.tix.feature.plan.domain.stats.TicketStats
import org.tix.feature.plan.domain.ticket.PlanningCompleteInfo
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.PlanningOperation
import org.tix.feature.plan.domain.ticket.TicketPlanningSystem
import org.tix.ticket.RenderedTicket

class DryRunPlanningSystem(
    private val ticketStats: TicketStats
) : TicketPlanningSystem<DryRunTicketPlanResult> {

    override suspend fun setup(context: PlanningContext<DryRunTicketPlanResult>) = Unit

    override suspend fun planTicket(
        context: PlanningContext<DryRunTicketPlanResult>,
        ticket: RenderedTicket,
        operation: PlanningOperation
    ): DryRunTicketPlanResult {
        ticketStats.countTicket(context.level)

        return DryRunTicketPlanResult(
            tixId = ticket.tixId,
            level = context.level,
            title = ticket.title,
            ticketType = ticketStats.capitalizedLabel(context.level, 1),
            body = ticket.body,
            fields = ticket.fields,
            operation = operation
        )
    }

    override suspend fun executeWorkFlow(
        workflow: Workflow,
        context: PlanningContext<*>
    ): Map<String, String> = emptyMap()

    override suspend fun completeInfo(): PlanningCompleteInfo = PlanningCompleteInfo(message = ticketStats.render())
}