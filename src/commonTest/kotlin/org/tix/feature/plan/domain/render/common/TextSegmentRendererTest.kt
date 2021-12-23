package org.tix.feature.plan.domain.render.common

import org.tix.ticket.body.TextSegment
import kotlin.test.Test
import kotlin.test.expect

class TextSegmentRendererTest {
    @Test
    fun render() {
        val segment = TextSegment("text")
        expect(segment.text) { TextSegmentRenderer().render(segment) }
    }
}