package org.tix.feature.plan.presentation.reducer

import org.tix.feature.plan.domain.state.PlanDomainState
import org.tix.feature.plan.presentation.state.CLIPlanViewState

class CLIPlanViewStateReducer: PlanViewStateReducer<CLIPlanViewState> {
    override suspend fun reduce(domainState: PlanDomainState) = CLIPlanViewState()
}