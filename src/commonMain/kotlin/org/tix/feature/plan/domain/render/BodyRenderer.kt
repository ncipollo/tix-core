package org.tix.feature.plan.domain.render

import org.tix.ticket.body.BodySegment
import org.tix.ticket.body.TicketBody
import org.tix.ticket.system.TicketSystemType

class BodyRenderer(private val ticketSystemType: TicketSystemType, builder: BodyRendererBuilder) {
    private val renderers = builder.build(this)
    internal val supportedRenderers = renderers.keys // For testing only

    fun render(body: TicketBody) =
        body.segments
            .joinToString(separator = "") { renderSegment(it) }

    private fun renderSegment(segment: BodySegment) =
        rendererForType(segment)?.render(segment) ?: missingRendererError(segment, ticketSystemType)

    @Suppress("UNCHECKED_CAST")
    private fun rendererForType(segment: BodySegment) =
        renderers[segment::class] as? BodySegmentRenderer<BodySegment>
}