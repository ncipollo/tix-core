package org.tix.feature.plan.domain.render.github

import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.ticket.body.LinkSegment

class GithubLinkRenderer: BodySegmentRenderer<LinkSegment> {
    override fun render(segment: LinkSegment) = "[${segment.title}](${segment.destination})"
}