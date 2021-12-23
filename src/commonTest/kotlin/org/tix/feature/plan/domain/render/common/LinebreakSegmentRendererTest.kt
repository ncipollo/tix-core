package org.tix.feature.plan.domain.render.common

import org.tix.ticket.body.LinebreakSegment
import kotlin.test.Test
import kotlin.test.expect

class LinebreakSegmentRendererTest {
    @Test
    fun render() {
        expect("\n") { LinebreakSegmentRenderer().render(LinebreakSegment) }
    }
}