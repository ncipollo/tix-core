package org.tix.feature.plan.domain.render.jira

import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.ticket.body.EmphasisSegment

class JiraEmphasisRenderer: BodySegmentRenderer<EmphasisSegment> {
    override fun render(segment: EmphasisSegment) = "_${segment.text}_"
}