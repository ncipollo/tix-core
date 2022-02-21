package org.tix.feature.plan.domain.ticket.jira

import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.PlanningOperation
import org.tix.integrations.jira.issue.Issue
import org.tix.integrations.jira.issue.IssueFields
import org.tix.ticket.RenderedTicket

class JiraIssueBuilder(
    fieldCache: JiraFieldCache
) {
    private val unknownsBuilder = JiraUnknownsBuilder(fieldCache)

    fun issue(
        ticket: RenderedTicket,
        context: PlanningContext<JiraPlanResult>,
        operation: PlanningOperation
    ) = Issue(
        key = operation.ticketKey,
        fields = fields(ticket, context)
    )

    private fun fields(ticket: RenderedTicket, context: PlanningContext<JiraPlanResult>): IssueFields =
        JiraIssueFieldsBuilder(
            context = context,
            summary = ticket.title,
            description = ticket.body,
            fields = ticket.fields,
            unknownsBuilder = unknownsBuilder
        ).issueFields()
}