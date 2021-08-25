package org.tix.feature.plan.domain.render.common

import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.model.ticket.body.WhitespaceSegment

class WhitespaceRenderer: BodySegmentRenderer<WhitespaceSegment> {
    override fun render(segment: WhitespaceSegment) = " ".repeat(segment.count)
}