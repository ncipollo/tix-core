package org.tix.feature.plan.domain.ticket.github.creator

import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.PlanningOperation
import org.tix.feature.plan.domain.ticket.github.GithubPlanResult
import org.tix.feature.plan.domain.ticket.github.cache.MilestoneCache
import org.tix.feature.plan.domain.ticket.github.result.resultMap
import org.tix.integrations.github.GithubApi
import org.tix.ticket.RenderedTicket

class IssueCreator(
    private val githubApi: GithubApi,
    private val issueLinker: IssueLinker,
    private val milestoneCache: MilestoneCache,
    private val requestBuilder: IssueRequestBuilder
) {
    suspend fun setup() {
        milestoneCache.populateCache()
    }

    suspend fun planTicket(
        context: PlanningContext<GithubPlanResult>,
        ticket: RenderedTicket,
        operation: PlanningOperation
    ): GithubPlanResult {
        val issue = performOperation(context, ticket, operation)
        return GithubPlanResult(
            id = issue.nodeId,
            key = "#${issue.number}",
            tixId = ticket.tixId,
            level = context.level,
            description = ticket.title,
            results = issue.resultMap(),
            operation = operation,
            startingLevel = context.startingLevel
        )
    }

    private suspend fun performOperation(
        context: PlanningContext<GithubPlanResult>,
        ticket: RenderedTicket,
        operation: PlanningOperation
    ) = when (operation) {
        PlanningOperation.CreateTicket -> createIssue(context, ticket)
        is PlanningOperation.DeleteTicket -> deleteIssue(operation)
        is PlanningOperation.UpdateTicket -> updateIssue(operation, ticket)
    }

    private suspend fun createIssue(context: PlanningContext<GithubPlanResult>, ticket: RenderedTicket) =
        githubApi.rest.issues.create(requestBuilder.createRequest(ticket))
            .also { issue ->
                issueLinker.linkIssueToProject(context, issue, ticket)
            }

    private suspend fun deleteIssue(operation: PlanningOperation) =
        githubApi.rest.issues.closeIssue(operation.ticketNumber)

    private suspend fun updateIssue(operation: PlanningOperation, ticket: RenderedTicket) =
        githubApi.rest.issues.update(operation.ticketNumber, requestBuilder.updateRequest(ticket))
}