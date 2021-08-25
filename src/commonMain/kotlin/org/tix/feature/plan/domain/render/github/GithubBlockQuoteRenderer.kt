package org.tix.feature.plan.domain.render.github

import org.tix.feature.plan.domain.render.BodyRenderer
import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.model.ticket.body.BlockQuoteSegment

class GithubBlockQuoteRenderer(private val bodyRenderer: BodyRenderer) : BodySegmentRenderer<BlockQuoteSegment> {
    override fun render(segment: BlockQuoteSegment) =
        if (segment.body.segments.isNotEmpty()) {
            bodyRenderer.render(segment.body)
                .split("\n")
                .joinToString(separator = "\n") { "> $it" }
        } else {
            ""
        }
}