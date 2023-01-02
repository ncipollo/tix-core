package org.tix.feature.plan.presentation.reducer

import org.tix.feature.plan.domain.state.PlanDomainState
import org.tix.feature.plan.presentation.state.PlanViewState

interface PlanViewStateReducer<VS: PlanViewState> {
    suspend fun reduce(domainState: PlanDomainState): VS
}