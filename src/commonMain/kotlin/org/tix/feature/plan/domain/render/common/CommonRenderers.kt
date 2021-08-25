package org.tix.feature.plan.domain.render.common

import org.tix.feature.plan.domain.render.BodyRendererBuilder

fun BodyRendererBuilder.commonRenderers() {
    renderer { BulletListRenderer(it) }
    renderer { LinebreakSegmentRenderer() }
    renderer { OrderedListRenderer(it) }
    renderer { ParagraphRenderer(it) }
    renderer { TextSegmentRenderer() }
    renderer { ThematicBreakRenderer() }
    renderer { WhitespaceRenderer() }
}