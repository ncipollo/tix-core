package org.tix.feature.plan.presentation.reducer

import org.tix.feature.plan.domain.state.*
import org.tix.feature.plan.presentation.state.CLIPlanViewState

class CLIPlanViewStateReducer: PlanViewStateReducer<CLIPlanViewState> {
    override suspend fun reduce(domainState: PlanDomainState) =
        when(domainState) {
            is PlanDomainCompleted -> CLIPlanViewState(isComplete = true)
            is PlanDomainError -> CLIPlanViewState(message = domainState.ex.toString())
            PlanDomainParsing -> CLIPlanViewState(message = "Parsing markdown")
            PlanDomainStartingTicketCreation -> CLIPlanViewState(message = "Creating tickets")
            is PlanDomainUpdate -> CLIPlanViewState(message = domainState.currentResult.description)
        }
}