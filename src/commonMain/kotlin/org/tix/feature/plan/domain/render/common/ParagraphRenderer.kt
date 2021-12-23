package org.tix.feature.plan.domain.render.common

import org.tix.feature.plan.domain.render.BodyRenderer
import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.ticket.body.ParagraphSegment

class ParagraphRenderer(private val bodyRenderer: BodyRenderer): BodySegmentRenderer<ParagraphSegment> {
    override fun render(segment: ParagraphSegment) = bodyRenderer.render(segment.body)
}