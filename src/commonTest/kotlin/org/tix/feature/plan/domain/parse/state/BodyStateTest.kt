package org.tix.feature.plan.domain.parse.state

import org.tix.model.ticket.body.BodySegment
import org.tix.model.ticket.body.LinebreakSegment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.expect

class BodyStateTest {
    private val baseBody = ArrayList<BodySegment>()
    private val bodyState = BodyState(baseBody)

    @Test
    fun addSegments_addsToBaseBody() {
        bodyState.addSegments(arrayOf(LinebreakSegment))
        assertEquals(listOf<BodySegment>(LinebreakSegment), baseBody)
    }

    @Test
    fun popBody() {
        bodyState.pushBody()
        bodyState.addSegments(arrayOf(LinebreakSegment))

        expect(listOf<BodySegment>(LinebreakSegment)) { bodyState.popBody() }
        expect(true) { baseBody.isEmpty() }
    }
}