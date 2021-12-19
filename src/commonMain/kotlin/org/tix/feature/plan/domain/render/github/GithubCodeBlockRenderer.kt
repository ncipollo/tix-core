package org.tix.feature.plan.domain.render.github

import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.model.ticket.body.CodeBlockSegment

class GithubCodeBlockRenderer: BodySegmentRenderer<CodeBlockSegment> {
    override fun render(segment: CodeBlockSegment) =
        if (segment.language.isNotBlank()) {
            "```${segment.language}${segment.code}```"
        } else {
            "```${segment.code}```"
        }
}