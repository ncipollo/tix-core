package org.tix.feature.plan.domain.combiner

import org.tix.error.toTixError
import org.tix.feature.plan.domain.state.PlanDomainCompleted
import org.tix.feature.plan.domain.state.PlanDomainError
import org.tix.feature.plan.domain.state.PlanDomainStartingTicketCreation
import org.tix.feature.plan.domain.state.PlanDomainUpdate
import org.tix.feature.plan.domain.ticket.*

object PlanStatusMapper {
    fun mapStatus(planStatus: TicketPlanStatus) =
        when(planStatus) {
            TicketPlanStarted -> PlanDomainStartingTicketCreation
            is TicketPlanCompleted -> PlanDomainCompleted(planStatus.info)
            is TicketPlanUpdated -> PlanDomainUpdate(planStatus.result)
            is TicketPlanFailed -> PlanDomainError(planStatus.throwable.toTixError())
        }
}