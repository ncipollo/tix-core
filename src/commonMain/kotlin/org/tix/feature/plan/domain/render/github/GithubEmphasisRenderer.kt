package org.tix.feature.plan.domain.render.github

import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.ticket.body.EmphasisSegment

class GithubEmphasisRenderer: BodySegmentRenderer<EmphasisSegment> {
    override fun render(segment: EmphasisSegment) = "*${segment.text}*"
}