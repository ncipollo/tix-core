package org.tix.feature.plan.domain.ticket

import kotlin.test.Test
import kotlin.test.expect

class TicketPlanResultTest {
    @Test
    fun wasDryRun() {
        val result = MockTicketPlanResult()
        expect(false) { result.wasDryRun }
    }
}