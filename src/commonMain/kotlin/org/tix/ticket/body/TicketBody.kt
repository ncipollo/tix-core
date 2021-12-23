package org.tix.ticket.body

import kotlinx.serialization.Serializable

@Serializable
data class TicketBody(val segments: List<BodySegment> = emptyList())

fun List<BodySegment>.toTicketBody() = TicketBody(segments = this)

fun emptyBody() = TicketBody()