package org.tix.feature.plan.domain.ticket

sealed class TicketPlanStatus

object TicketPlanStarted : TicketPlanStatus()
data class TicketPlanFailed(val throwable: Throwable): TicketPlanStatus()
data class TicketPlanUpdated(val result: TicketPlanResult) : TicketPlanStatus()
data class TicketPlanCompleted(val info: PlanningCompleteInfo = PlanningCompleteInfo()) : TicketPlanStatus()
