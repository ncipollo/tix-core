package org.tix.integrations.jira.issue

import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.tix.fixture.integrations.jiraApi
import org.tix.integrations.jira.project.Project
import kotlin.test.Ignore
import kotlin.test.expect

@Ignore
class IssueApiTest {
    private companion object {
        val CREATED_FIELDS = IssueFields(
            summary = "Created Ticket",
            description = "This is a test",
            project = Project(key = "TIX"),
            type = IssueType(name = "Story")
        )
        val CREATED_ISSUE = Issue(fields = CREATED_FIELDS)
        val UPDATED_FIELDS = CREATED_FIELDS.copy(summary = "Updated Ticket")
        val UPDATED_ISSUE = Issue(fields = UPDATED_FIELDS)
    }
    private val api = jiraApi().issue

    @Test
    fun create_update_delete() = runTest {
        val createdResult = api.create(CREATED_ISSUE)
        val createdIssue = api.get(createdResult.key)
        expect(CREATED_FIELDS.summary) { createdIssue.fields?.summary }

        val updatedResult = api.update(UPDATED_ISSUE.copy(key = createdIssue.key))
        val updatedIssue = api.get(updatedResult.key)
        expect(UPDATED_FIELDS.summary) { updatedIssue.fields?.summary }

        api.delete(updatedIssue.key)
    }

    @Test
    fun get() = runTest {
        val issue = api.get("TIX-978")
        expect("First Ticket") { issue.fields!!.summary }
    }

    @Test
    fun search() = runTest {
        val issues = api.search("summary ~ \"first\"")
        expect("First Ticket") { issues.first().fields!!.summary }
    }
}