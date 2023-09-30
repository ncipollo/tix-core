package org.tix.feature.plan.domain.ticket

import org.tix.feature.plan.domain.render.BodyRenderer
import org.tix.feature.plan.domain.transform.TransformVariableMap
import org.tix.feature.plan.domain.transform.transform
import org.tix.platform.Env
import org.tix.ticket.RenderedTicket
import org.tix.ticket.Ticket

class TicketTransformer(
    private val context: PlanningContext<*>,
    env: Env,
    private val renderer: BodyRenderer,
    private val ticket: Ticket
) {
    private val variables = context.variables + ticket.variables
    private val variableMap = TransformVariableMap(env, variables, context.variableToken)

    fun ticket() =
        RenderedTicket(
            title = ticket.title.transform(variableMap),
            body = renderer.render(ticket.body).transform(variableMap),
            fields = ticket.mergedFields(context.config, context.level).transform(variableMap),
            tixId = ticket.tixId
        )
}