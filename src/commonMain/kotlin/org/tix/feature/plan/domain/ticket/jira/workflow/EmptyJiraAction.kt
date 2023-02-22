package org.tix.feature.plan.domain.ticket.jira.workflow

import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.PlanningContext

object EmptyJiraAction: JiraAction {
    override suspend fun execute(action: Action, context: PlanningContext<*>) = emptyMap<String, String>()
}