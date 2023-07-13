package org.tix.feature.plan.domain.ticket.github.result

import org.tix.integrations.github.graphql.project.ProjectV2Node
import org.tix.integrations.github.rest.issue.Issue
import kotlin.test.Test
import kotlin.test.assertEquals

class GithubResultMapBuilderTest {
    @Test
    fun issue_resultMap() {
        val issue = Issue(number = 42)
        val expected = mapOf(
            "ticket.github.previous.key" to "#42",
            "ticket.github.key" to "#42"
        )
        assertEquals(expected, issue.resultMap())
    }

    @Test
    fun project_resultMap() {
        val project = ProjectV2Node(number = 42)
        val expected = mapOf(
            "ticket.github.previous.key" to "#42",
            "ticket.github.key" to "#42"
        )
        assertEquals(expected, project.resultMap())
    }
}