package org.tix.feature.plan.domain.render.common

import org.tix.ticket.body.WhitespaceSegment
import kotlin.test.Test
import kotlin.test.expect

class WhitespaceRendererTest {
    @Test
    fun render() {
        val segment = WhitespaceSegment(4)
        expect("    ") { WhitespaceRenderer().render(segment) }
    }
}