package org.tix.feature.plan.domain.ticket

import kotlin.test.Test
import kotlin.test.expect

class PlanningOperationTest {
    @Test
    fun ticketNumber() {
        expect(42) { PlanningOperation.UpdateTicket("#42").ticketNumber }
        expect(42) { PlanningOperation.DeleteTicket("#42").ticketNumber }
    }
}