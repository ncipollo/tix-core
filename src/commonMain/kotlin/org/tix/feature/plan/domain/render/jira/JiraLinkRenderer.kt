package org.tix.feature.plan.domain.render.jira

import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.model.ticket.body.LinkSegment

class JiraLinkRenderer: BodySegmentRenderer<LinkSegment> {
    override fun render(segment: LinkSegment) = "[${segment.title}|${segment.destination}]"
}