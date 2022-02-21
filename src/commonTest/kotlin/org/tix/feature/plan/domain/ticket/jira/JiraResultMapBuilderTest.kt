package org.tix.feature.plan.domain.ticket.jira

import org.tix.integrations.jira.issue.Issue
import kotlin.test.Test
import kotlin.test.expect

class JiraResultMapBuilderTest {
    @Test
    fun issue_resultMap() {
        val issue = Issue(key = "key")
        val expected = mapOf(
            "ticket.jira.key" to "key",
            "ticket.jira.previous.key" to "key",
        )
        expect(expected) { issue.resultMap() }
    }
}