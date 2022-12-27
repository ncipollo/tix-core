package org.tix.feature.plan.presentation.reducer

import org.tix.feature.plan.domain.state.PlanDomainState
import org.tix.feature.plan.presentation.PlanViewState

interface PlanViewStateReducer {
    suspend fun reduce(domainState: PlanDomainState): PlanViewState
}