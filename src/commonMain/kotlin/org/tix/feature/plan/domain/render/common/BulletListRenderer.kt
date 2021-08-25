package org.tix.feature.plan.domain.render.common

import org.tix.feature.plan.domain.render.BodyRenderer
import org.tix.feature.plan.domain.render.BodySegmentRenderer
import org.tix.model.ticket.body.BulletListSegment

class BulletListRenderer(private val bodyRenderer: BodyRenderer): BodySegmentRenderer<BulletListSegment> {
    override fun render(segment: BulletListSegment) = bodyRenderer.render(segment.body)
}