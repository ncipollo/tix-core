package org.tix.model.ticket

import kotlinx.serialization.Serializable
import org.tix.model.ticket.body.TicketBody
import org.tix.model.ticket.body.emptyBody
import org.tix.model.ticket.field.TicketFields

@Serializable
data class Ticket(
    val title: String = "",
    val body: TicketBody = emptyBody(),
    val fields: TicketFields = TicketFields(),
    val children: List<Ticket> = emptyList()
)
