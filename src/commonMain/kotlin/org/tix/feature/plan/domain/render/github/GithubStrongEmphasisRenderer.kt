package org.tix.feature.plan.domain.render.github

import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.ticket.body.EmphasisSegment
import org.tix.ticket.body.StrongEmphasisSegment

class GithubStrongEmphasisRenderer: BodySegmentRenderer<StrongEmphasisSegment> {
    override fun render(segment: StrongEmphasisSegment) = "**${segment.text}**"
}