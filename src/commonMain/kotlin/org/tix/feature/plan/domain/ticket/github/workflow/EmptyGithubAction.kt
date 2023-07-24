package org.tix.feature.plan.domain.ticket.github.workflow

import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.PlanningContext

object EmptyGithubAction: GithubAction {
    override suspend fun execute(action: Action, context: PlanningContext<*>) = emptyMap<String, String>()
}