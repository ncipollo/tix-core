package org.tix.model.ticket

import org.tix.model.ticket.body.BodySegment

data class Ticket(val title: String = "", val body: List<BodySegment> = emptyList())
