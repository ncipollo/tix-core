package org.tix.feature.plan.domain.render.common

import org.tix.model.ticket.body.TextSegment
import org.tix.model.ticket.body.ThematicBreakSegment
import kotlin.test.Test
import kotlin.test.expect

class ThematicBreakRendererTest {
    @Test
    fun render() {
        expect("----") { ThematicBreakRenderer().render(ThematicBreakSegment) }
    }
}