package org.tix.feature.plan.domain.render.common

import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.model.ticket.body.TextSegment
import org.tix.model.ticket.body.ThematicBreakSegment

class ThematicBreakRenderer: BodySegmentRenderer<ThematicBreakSegment> {
    override fun render(segment: ThematicBreakSegment) = "----"
}