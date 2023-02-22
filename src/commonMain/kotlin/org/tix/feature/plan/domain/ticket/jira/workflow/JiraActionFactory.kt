package org.tix.feature.plan.domain.ticket.jira.workflow

import org.tix.config.data.Action
import org.tix.integrations.jira.JiraApi

class JiraActionFactory(private val api: JiraApi) {
    fun jiraAction(action: Action) =
        when(action.type) {
            "delete_issue" -> JiraDeleteIssueAction(api)
            else -> EmptyJiraAction
        }
}