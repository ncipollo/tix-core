package org.tix.feature.plan.parse.state

import org.tix.model.ticket.Ticket
import org.tix.model.ticket.body.BodySegment
import org.tix.model.ticket.body.TicketBody

data class PartialTicket(
    var title: String = "",
    var body: MutableList<BodySegment> = ArrayList(),
    var children: MutableList<PartialTicket> = ArrayList()
) {
    fun ticket(): Ticket = Ticket(
        title = title,
        body = TicketBody(body),
        children = children.map { it.ticket() }
    )
}
