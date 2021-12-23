package org.tix.feature.plan.domain.render

import org.tix.ticket.body.BodySegment
import org.tix.ticket.system.TicketSystemType

class MissingRendererException(
    segment: BodySegment,
    ticketSystem: TicketSystemType
) : RuntimeException(errorMessage(segment, ticketSystem))

private fun errorMessage(segment: BodySegment, ticketSystem: TicketSystemType) =
    "Missing ${segment::class.simpleName} renderer for ${ticketSystem.name.lowercase()}"

fun missingRendererError(segment: BodySegment, ticketSystem: TicketSystemType): Nothing =
    throw (MissingRendererException(segment, ticketSystem))