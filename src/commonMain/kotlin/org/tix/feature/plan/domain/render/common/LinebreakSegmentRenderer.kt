package org.tix.feature.plan.domain.render.common

import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.model.ticket.body.LinebreakSegment

class LinebreakSegmentRenderer: BodySegmentRenderer<LinebreakSegment> {
    override fun render(segment: LinebreakSegment) = "\n"
}