package org.tix.feature.plan.domain.render.github

import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.ticket.body.CodeSpanSegment

class GithubCodeSpanRenderer: BodySegmentRenderer<CodeSpanSegment> {
    override fun render(segment: CodeSpanSegment) = "`${segment.code}`"
}