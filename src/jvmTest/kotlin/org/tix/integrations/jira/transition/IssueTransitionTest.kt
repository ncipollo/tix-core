package org.tix.integrations.jira.transition

import kotlinx.coroutines.test.runTest
import org.junit.Ignore
import org.junit.Test
import org.tix.fixture.integrations.jiraApi
import org.tix.integrations.jira.issue.Issue
import org.tix.integrations.jira.issue.IssueFields
import org.tix.integrations.jira.issue.IssueType
import org.tix.integrations.jira.project.Project

@Ignore
class IssueTransitionTest {
    private companion object {
        val FIELDS = IssueFields(
            summary = "Transition test",
            description = "This is a test",
            project = Project(key = "TIX"),
            type = IssueType(name = "Story")
        )
        val ISSUE = Issue(fields = FIELDS)
    }

    private val api = jiraApi()

    @Test
    fun transitionIssue() = runTest {
        val issueResult = api.issue.create(ISSUE)

        val transitions = transitionsByName(issueResult.keyOrId)
        val transitionId = transitions["Done"]!!.id

        api.transition.transitionIssue(issueResult.keyOrId, transitionId)

        api.issue.delete(issueResult.keyOrId)
    }

    private suspend fun transitionsByName(issueId: String) =
        api.transition.transitions(issueId).associateBy { it.name }
}