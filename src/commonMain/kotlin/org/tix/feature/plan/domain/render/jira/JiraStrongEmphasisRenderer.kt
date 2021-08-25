package org.tix.feature.plan.domain.render.jira

import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.model.ticket.body.EmphasisSegment
import org.tix.model.ticket.body.StrongEmphasisSegment

class JiraStrongEmphasisRenderer: BodySegmentRenderer<StrongEmphasisSegment> {
    override fun render(segment: StrongEmphasisSegment) = "*${segment.text}*"
}