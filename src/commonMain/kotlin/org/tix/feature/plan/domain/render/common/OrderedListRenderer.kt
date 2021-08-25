package org.tix.feature.plan.domain.render.common

import org.tix.feature.plan.domain.render.BodyRenderer
import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.model.ticket.body.OrderedListSegment

class OrderedListRenderer(private val bodyRenderer: BodyRenderer): BodySegmentRenderer<OrderedListSegment> {
    override fun render(segment: OrderedListSegment) = bodyRenderer.render(segment.body)
}