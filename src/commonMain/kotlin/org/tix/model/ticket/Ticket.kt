package org.tix.model.ticket

import kotlinx.serialization.Serializable
import org.tix.model.ticket.body.TicketBody
import org.tix.model.ticket.body.emptyBody
import org.tix.serialize.dynamic.DynamicElement
import org.tix.serialize.dynamic.emptyDynamic

@Serializable
data class Ticket(
    val title: String = "",
    val body: TicketBody = emptyBody(),
    val fields: DynamicElement = emptyDynamic(),
    val children: List<Ticket> = emptyList()
)
