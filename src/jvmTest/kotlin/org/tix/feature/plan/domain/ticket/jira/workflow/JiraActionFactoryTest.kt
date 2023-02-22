package org.tix.feature.plan.domain.ticket.jira.workflow

import io.mockk.mockk
import org.junit.Test
import org.tix.config.data.Action
import org.tix.integrations.jira.JiraApi
import kotlin.test.expect

class JiraActionFactoryTest {
    private val jiraApi = mockk<JiraApi>()
    private val actionFactory = JiraActionFactory(jiraApi)

    @Test
    fun jiraAction() {
        val actions = listOf(
            Action("delete_issue")
        )

        val expectedTypes = listOf(
            JiraDeleteIssueAction::class
        )
        expect(expectedTypes) {
            actions.map { actionFactory.jiraAction(it)::class }
        }
    }
}