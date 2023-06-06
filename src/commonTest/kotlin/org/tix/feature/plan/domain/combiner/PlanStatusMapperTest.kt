package org.tix.feature.plan.domain.combiner

import org.tix.error.toTixError
import org.tix.feature.plan.domain.error.TicketPlanningException
import org.tix.feature.plan.domain.state.PlanDomainCompleted
import org.tix.feature.plan.domain.state.PlanDomainError
import org.tix.feature.plan.domain.state.PlanDomainStartingTicketCreation
import org.tix.feature.plan.domain.state.PlanDomainUpdate
import org.tix.feature.plan.domain.ticket.*
import kotlin.test.Test
import kotlin.test.expect

class PlanStatusMapperTest {
    @Test
    fun mapStatus_completed() {
        val info = PlanningCompleteInfo(message = "done")
        expect(PlanDomainCompleted(info)) {
            PlanStatusMapper.mapStatus(TicketPlanCompleted(info))
        }
    }

    @Test
    fun mapStatus_error() {
        val error = TicketPlanningException("fail")
        expect(PlanDomainError(error.toTixError())) {
            PlanStatusMapper.mapStatus(TicketPlanFailed(error))
        }
    }

    @Test
    fun mapStatus_startingTicketCreation() {
        expect(PlanDomainStartingTicketCreation) {
            PlanStatusMapper.mapStatus(TicketPlanStarted)
        }
    }

    @Test
    fun mapStatus_update() {
        val result = MockTicketPlanResult(id = "id")
        expect(PlanDomainUpdate(result)) {
            PlanStatusMapper.mapStatus(TicketPlanUpdated(result))
        }
    }
}