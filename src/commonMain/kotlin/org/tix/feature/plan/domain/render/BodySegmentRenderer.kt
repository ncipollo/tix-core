package org.tix.feature.plan.domain.render

import org.tix.ticket.body.BodySegment

interface BodySegmentRenderer<T : BodySegment> {
    fun render(segment: T): String
}