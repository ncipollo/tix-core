package org.tix.feature.plan.domain.render.github

import org.tix.feature.plan.domain.render.BodyRenderer
import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.model.ticket.body.BulletListItemSegment
import org.tix.model.ticket.body.OrderedListItemSegment

class GithubOrderedItemRenderer(private val bodyRenderer: BodyRenderer): BodySegmentRenderer<OrderedListItemSegment> {
    override fun render(segment: OrderedListItemSegment): String {
        val indent = "    ".repeat(segment.level)
        return "${indent}1. ${bodyRenderer.render(segment.body)}"
    }
}