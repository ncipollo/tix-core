package org.tix.feature.plan.domain.ticket.jira.workflow

import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.integrations.jira.JiraApi

class JiraDeleteIssueAction(private val api: JiraApi): JiraAction {
    override suspend fun execute(action: Action, context: PlanningContext<*>): Map<String, String> {
        val issueId = action.arguments["issue"] as? String
        issueId?.let {
            api.issue.delete(it)
        }
        return emptyMap()
    }
}