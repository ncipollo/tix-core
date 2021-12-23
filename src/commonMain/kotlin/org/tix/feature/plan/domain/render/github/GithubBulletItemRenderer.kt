package org.tix.feature.plan.domain.render.github

import org.tix.feature.plan.domain.render.BodyRenderer
import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.ticket.body.BulletListItemSegment

class GithubBulletItemRenderer(private val bodyRenderer: BodyRenderer): BodySegmentRenderer<BulletListItemSegment> {
    override fun render(segment: BulletListItemSegment): String {
        val indent = "    ".repeat(segment.level)
        return "$indent${segment.marker} ${bodyRenderer.render(segment.body)}"
    }
}