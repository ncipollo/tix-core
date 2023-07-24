package org.tix.feature.plan.domain.ticket.github.workflow

import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.ticketNumber
import org.tix.integrations.github.GithubApi

class GithubCloseIssueAction(private val api: GithubApi): GithubAction {
    override suspend fun execute(action: Action, context: PlanningContext<*>): Map<String, String> {
        val issueNumber = ticketNumber(action.arguments["issue"])
        api.rest.issues.closeIssue(issueNumber)
        return emptyMap()
    }
}