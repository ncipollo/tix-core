package org.tix.feature.plan.domain.state

import org.tix.error.TixError
import org.tix.feature.plan.domain.ticket.PlanningCompleteInfo
import org.tix.feature.plan.domain.ticket.TicketPlanResult
import org.tix.feature.plan.domain.ticket.TicketPlanStatus

sealed class PlanDomainState
object PlanDomainParsing: PlanDomainState()
object PlanDomainStartingTicketCreation: PlanDomainState()
data class PlanDomainUpdate(val currentResult: TicketPlanResult): PlanDomainState()
data class PlanDomainCompleted(val info: PlanningCompleteInfo = PlanningCompleteInfo()): PlanDomainState()
data class PlanDomainError(val ex: TixError): PlanDomainState()

