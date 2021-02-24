package org.tix.feature.plan.parse.state

import org.tix.model.ticket.Ticket
import org.tix.model.ticket.body.BodySegment
import org.tix.model.ticket.body.TicketBody
import org.tix.model.ticket.field.TicketFields

internal data class PartialTicket(
    var title: String = "",
    var body: MutableList<BodySegment> = ArrayList(),
    val fields: TicketFields = TicketFields(),
    var children: MutableList<PartialTicket> = ArrayList(),
) : BodyBuilder  {
    private val bodyState: BodyState = BodyState(body)

    override fun addSegments(bodySegments: Array<out BodySegment>) = bodyState.addSegments(bodySegments)

    fun buildNestedBody(buildBlock: () -> Unit) : List<BodySegment> {
        bodyState.pushBody()
        buildBlock()
        return bodyState.popBody()
    }

    fun ticket(): Ticket = Ticket(
        title = title,
        body = TicketBody(body),
        fields = fields,
        children = children.map { it.ticket() }
    )
}
