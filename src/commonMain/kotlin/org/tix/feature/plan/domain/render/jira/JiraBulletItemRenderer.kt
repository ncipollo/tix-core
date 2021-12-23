package org.tix.feature.plan.domain.render.jira

import org.tix.feature.plan.domain.render.BodyRenderer
import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.ticket.body.BulletListItemSegment

class JiraBulletItemRenderer(private val bodyRenderer: BodyRenderer): BodySegmentRenderer<BulletListItemSegment> {
    override fun render(segment: BulletListItemSegment): String {
        val markers = segment.marker.repeat(segment.level + 1)
        return "$markers ${bodyRenderer.render(segment.body)}"
    }
}