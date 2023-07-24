package org.tix.feature.plan.domain.ticket.github.workflow

import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.PlanningContext

interface GithubAction {
    suspend fun execute(action: Action, context: PlanningContext<*>): Map<String, String>
}

