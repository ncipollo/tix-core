package org.tix.feature.plan.domain.ticket.jira

import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.PlanningOperation
import org.tix.integrations.jira.field.Field
import org.tix.integrations.jira.issue.Issue
import org.tix.ticket.RenderedTicket
import kotlin.test.Test
import kotlin.test.expect

class JiraIssueBuilderTest {
    private val cache = JiraFieldCache(listOf(Field(id = "1", name = "unknown")))
    private val context = PlanningContext<JiraPlanResult>()
    private val fields = mapOf(
        "affects_versions" to listOf("1.0", "2.0", "3.0"),
        "components" to listOf("component1", "component2"),
        "fix_versions" to listOf("1.1", "2.1", "3.1"),
        "labels" to listOf("label1", "label2"),
        "project" to "tix",
        "type" to "story",
        "unknown" to "foo"
    )
    private val issueBuilder = JiraIssueBuilder(cache)
    private val ticket = RenderedTicket(
        title = "title",
        body = "body",
        fields = fields
    )

    @Test
    fun issue_createOperation() {
        val issueFields = fieldBuilder()
        val expected = Issue(fields = issueFields)
        expect(expected) { issueBuilder.issue(ticket, context, PlanningOperation.CreateTicket) }
    }

    @Test
    fun issue_deleteOperation() {
        val issueFields = fieldBuilder()
        val expected = Issue(key = "key", fields = issueFields)
        expect(expected) { issueBuilder.issue(ticket, context, PlanningOperation.DeleteTicket("key")) }
    }

    @Test
    fun issue_updateOperation() {
        val issueFields = fieldBuilder()
        val expected = Issue(key = "key", fields = issueFields)
        expect(expected) { issueBuilder.issue(ticket, context, PlanningOperation.UpdateTicket("key")) }
    }

    private fun fieldBuilder() =
        JiraIssueFieldsBuilder(
            context = context,
            summary = "title",
            description = "body",
            fields = ticket.fields,
            unknownsBuilder = JiraUnknownsBuilder(cache)
        ).issueFields()

}