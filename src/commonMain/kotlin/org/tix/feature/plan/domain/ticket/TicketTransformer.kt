package org.tix.feature.plan.domain.ticket

import org.tix.feature.plan.domain.render.BodyRenderer
import org.tix.ticket.RenderedTicket
import org.tix.ticket.Ticket

class TicketTransformer(
    private val context: PlanningContext<*>,
    private val renderer: BodyRenderer,
    private val ticket: Ticket
) {
    fun ticket() =
        RenderedTicket(
            title = ticket.title,
            body = renderer.render(ticket.body),
            fields = ticket.mergedFields(context.config, context.level)
        )
}