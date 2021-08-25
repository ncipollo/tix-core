package org.tix.feature.plan.domain.render.common

import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.model.ticket.body.TextSegment

class TextSegmentRenderer: BodySegmentRenderer<TextSegment> {
    override fun render(segment: TextSegment) = segment.text
}