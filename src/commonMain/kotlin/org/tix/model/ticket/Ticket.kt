package org.tix.model.ticket

import kotlinx.serialization.Serializable
import org.tix.model.ticket.body.TicketBody
import org.tix.model.ticket.body.emptyBody

@Serializable
data class Ticket(
    val title: String = "",
    val body: TicketBody = emptyBody(),
    val children: List<Ticket> = emptyList()
)
