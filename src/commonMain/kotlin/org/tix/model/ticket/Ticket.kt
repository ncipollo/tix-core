package org.tix.model.ticket

import kotlinx.serialization.Serializable
import org.tix.model.ticket.body.BodySegment

@Serializable
data class Ticket(val title: String = "", val body: List<BodySegment> = emptyList())
