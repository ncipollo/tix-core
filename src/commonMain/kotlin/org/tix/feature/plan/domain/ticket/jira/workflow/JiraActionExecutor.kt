package org.tix.feature.plan.domain.ticket.jira.workflow

import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.integrations.jira.JiraApi

class JiraActionExecutor(private val api: JiraApi) {
    suspend fun execute(action: Action, context: PlanningContext<*>) = when (action.type) {
        "delete_issue" -> JiraDeleteIssueAction(api).execute(action)
        else -> emptyMap()
    }
}