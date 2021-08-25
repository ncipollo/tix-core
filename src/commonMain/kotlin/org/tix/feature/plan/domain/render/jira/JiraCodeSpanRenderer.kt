package org.tix.feature.plan.domain.render.jira

import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.model.ticket.body.CodeSpanSegment

class JiraCodeSpanRenderer: BodySegmentRenderer<CodeSpanSegment> {
    override fun render(segment: CodeSpanSegment) = "{{${segment.code}}}"
}