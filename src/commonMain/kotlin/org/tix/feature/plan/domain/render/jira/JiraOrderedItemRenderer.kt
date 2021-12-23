package org.tix.feature.plan.domain.render.jira

import org.tix.feature.plan.domain.render.BodyRenderer
import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.ticket.body.BulletListItemSegment
import org.tix.ticket.body.OrderedListItemSegment
import org.tix.ticket.body.OrderedListSegment

class JiraOrderedItemRenderer(private val bodyRenderer: BodyRenderer): BodySegmentRenderer<OrderedListItemSegment> {
    override fun render(segment: OrderedListItemSegment): String {
        val markers = "#".repeat(segment.level + 1)
        return "$markers ${bodyRenderer.render(segment.body)}"
    }
}