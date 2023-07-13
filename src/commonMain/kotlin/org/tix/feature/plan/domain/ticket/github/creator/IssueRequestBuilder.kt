package org.tix.feature.plan.domain.ticket.github.creator

import org.tix.ext.transformFilteredList
import org.tix.feature.plan.domain.ticket.github.GithubTicketSystemFields
import org.tix.feature.plan.domain.ticket.github.cache.MilestoneCache
import org.tix.integrations.github.rest.issue.IssueCreateRequest
import org.tix.integrations.github.rest.issue.IssueUpdateRequest
import org.tix.ticket.RenderedTicket

class IssueRequestBuilder(private val milestoneCache: MilestoneCache) {
    suspend fun createRequest(renderedTicket: RenderedTicket) =
        IssueCreateRequest(
            title = renderedTicket.title,
            body = renderedTicket.body,
            assignees = renderedTicket.assignees,
            milestone = renderedTicket.milestoneNumber(),
            labels = renderedTicket.labels
        )

    suspend fun updateRequest(renderedTicket: RenderedTicket) =
        IssueUpdateRequest(
            title = renderedTicket.title,
            body = renderedTicket.body,
            assignees = renderedTicket.assignees,
            milestone = renderedTicket.milestoneNumber(),
            labels = renderedTicket.labels
        )

    private val RenderedTicket.assignees
        get() = fields.transformFilteredList<String, String>(GithubTicketSystemFields.assignees) { it }

    private val RenderedTicket.labels
        get() = fields.transformFilteredList<String, String>(GithubTicketSystemFields.labels) { it }

    private suspend fun RenderedTicket.milestoneNumber() =
        fields[GithubTicketSystemFields.milestone]
            ?.toString()
            ?.let { milestoneCache.getOrCreateMilestone(it) }
            ?.number
}