package org.tix.feature.plan.domain.ticket.jira.workflow

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.mockPlanningContext
import org.tix.integrations.jira.JiraApi
import org.tix.integrations.jira.issue.IssueApi
import kotlin.test.assertEquals

class JiraDeleteIssueActionTest {
    private val action = Action(type = "delete_issue", arguments = mapOf("issue" to "issue1"))
    private val context = mockPlanningContext()
    private val issueApi = mockk<IssueApi>(relaxed = true)
    private val jiraApi = mockk<JiraApi> {
        every { issue } returns issueApi
    }

    private val jiraAction = JiraDeleteIssueAction(jiraApi)

    @Test
    fun execute() = runBlocking {
        val result = jiraAction.execute(action, context)

        assertEquals(emptyMap(), result)
        coVerify { issueApi.delete("issue1") }
    }
}