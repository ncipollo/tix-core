package org.tix.model.ticket.body

import kotlinx.serialization.Serializable

@Serializable
data class TicketBody(val segments: List<BodySegment> = emptyList())

fun emptyBody() = TicketBody()