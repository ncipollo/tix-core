package org.tix.feature.plan.parse.state

import org.tix.model.ticket.body.BodySegment
import org.tix.model.ticket.body.LineBreakSegment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.expect

class BodyStateTest {
    private val baseBody = ArrayList<BodySegment>()
    private val bodyState = BodyState(baseBody)

    @Test
    fun addSegments_addsToBaseBody() {
        bodyState.addSegments(arrayOf(LineBreakSegment))
        assertEquals(listOf<BodySegment>(LineBreakSegment), baseBody)
    }

    @Test
    fun popBody() {
        bodyState.pushBody()
        bodyState.addSegments(arrayOf(LineBreakSegment))

        expect(listOf<BodySegment>(LineBreakSegment)) { bodyState.popBody() }
        expect(true) { baseBody.isEmpty() }
    }
}