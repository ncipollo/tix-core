package org.tix.feature.plan.domain.render.jira

import org.tix.feature.plan.domain.render.BodyRenderer
import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.model.ticket.body.BlockQuoteSegment

class JiraBlockQuoteRenderer(private val bodyRenderer: BodyRenderer) : BodySegmentRenderer<BlockQuoteSegment> {
    override fun render(segment: BlockQuoteSegment) =
        if (segment.body.segments.isNotEmpty()) {
            "{quote}\n${bodyRenderer.render(segment.body)}\n{quote}"
        } else {
            ""
        }
}