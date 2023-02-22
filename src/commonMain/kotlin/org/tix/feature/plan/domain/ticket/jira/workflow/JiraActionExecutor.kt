package org.tix.feature.plan.domain.ticket.jira.workflow

import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.integrations.jira.JiraApi

class JiraActionExecutor(
    jiraApi: JiraApi,
    private val actionFactory: JiraActionFactory = JiraActionFactory(jiraApi)
) {
    suspend fun execute(action: Action, context: PlanningContext<*>) =
        actionFactory.jiraAction(action)
            .execute(action, context)
}