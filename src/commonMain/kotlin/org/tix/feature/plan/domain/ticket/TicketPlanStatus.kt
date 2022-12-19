package org.tix.feature.plan.domain.ticket

import org.tix.feature.plan.domain.error.TicketPlanningException

sealed class TicketPlanStatus

object TicketPlanStarted : TicketPlanStatus()
data class TicketPlanFailed(val throwable: TicketPlanningException): TicketPlanStatus()
data class TicketPlanUpdated(val result: TicketPlanResult) : TicketPlanStatus()
data class TicketPlanCompleted(val info: PlanningCompleteInfo = PlanningCompleteInfo()) : TicketPlanStatus()
