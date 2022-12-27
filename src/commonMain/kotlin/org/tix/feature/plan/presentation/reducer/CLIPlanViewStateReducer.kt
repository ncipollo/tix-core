package org.tix.feature.plan.presentation.reducer

import org.tix.feature.plan.domain.state.PlanDomainState
import org.tix.feature.plan.presentation.PlanViewState

class CLIPlanViewStateReducer: PlanViewStateReducer {
    override suspend fun reduce(domainState: PlanDomainState) = PlanViewState()
}