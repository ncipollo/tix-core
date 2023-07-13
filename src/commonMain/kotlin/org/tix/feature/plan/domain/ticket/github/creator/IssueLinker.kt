package org.tix.feature.plan.domain.ticket.github.creator

import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.github.GithubPlanResult
import org.tix.feature.plan.domain.ticket.github.GithubTicketSystemFields
import org.tix.feature.plan.domain.ticket.github.cache.ProjectCache
import org.tix.feature.plan.domain.ticket.ticketNumber
import org.tix.integrations.github.GithubApi
import org.tix.integrations.github.rest.issue.Issue
import org.tix.ticket.RenderedTicket

class IssueLinker(
    private val githubApi: GithubApi,
    private val projectCache: ProjectCache
) {
    private val projectQueries = githubApi.queries.projects

    suspend fun linkIssueToProject(
        context: PlanningContext<GithubPlanResult>,
        issue: Issue,
        ticket: RenderedTicket
    ) {
        val parentId = findBestParentId(context, ticket)
        parentId?.let {
            projectQueries.addItemToProject(parentId, issue.nodeId)
        }
    }

    private suspend fun findBestParentId(context: PlanningContext<GithubPlanResult>, ticket: RenderedTicket): String? {
        val explicitParentId = ticket.parentId()
        return explicitParentId ?: context.parentTicket?.id
    }

    private suspend fun RenderedTicket.parentId() =
        fields[GithubTicketSystemFields.parent]
            ?.toString()
            ?.let { ticketNumber(it) }
            ?.let { projectCache.getProject(it).id }
}