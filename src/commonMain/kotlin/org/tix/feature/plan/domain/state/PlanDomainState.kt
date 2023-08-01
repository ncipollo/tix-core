package org.tix.feature.plan.domain.state

import org.tix.error.TixError
import org.tix.feature.plan.domain.parse.MarkdownSource
import org.tix.feature.plan.domain.ticket.PlanningCompleteInfo
import org.tix.feature.plan.domain.ticket.TicketPlanResult

sealed class PlanDomainState
data class PlanDomainParsing(val markdownSource: MarkdownSource): PlanDomainState()
object PlanDomainStartingTicketCreation: PlanDomainState()
data class PlanDomainUpdate(val currentResult: TicketPlanResult): PlanDomainState()
data class PlanDomainCompleted(val info: PlanningCompleteInfo = PlanningCompleteInfo()): PlanDomainState()
data class PlanDomainError(val ex: TixError): PlanDomainState()

