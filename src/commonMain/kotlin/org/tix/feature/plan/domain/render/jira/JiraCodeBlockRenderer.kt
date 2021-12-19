package org.tix.feature.plan.domain.render.jira

import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.model.ticket.body.CodeBlockSegment

class JiraCodeBlockRenderer: BodySegmentRenderer<CodeBlockSegment> {
    override fun render(segment: CodeBlockSegment) =
        if (segment.language.isNotBlank()) {
            "{code:${segment.language}}${segment.code}{code}"
        } else {
            "{code}${segment.code}{code}"
        }
}