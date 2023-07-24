package org.tix.feature.plan.domain.ticket.github.workflow

import io.mockk.mockk
import org.junit.Test
import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.github.cache.ProjectCache
import org.tix.integrations.github.GithubApi
import kotlin.test.expect

class GithubActionFactoryTest {
    private val githubApi = mockk<GithubApi>()
    private val projectCache = mockk<ProjectCache>()
    private val actionFactory = GithubActionFactory(githubApi, projectCache)

    @Test
    fun jiraAction() {
        val actions = listOf(
            Action("close_issue"),
            Action("delete_project")
        )

        val expectedTypes = listOf(
            GithubCloseIssueAction::class,
            GithubDeleteProjectAction::class
        )
        expect(expectedTypes) {
            actions.map { actionFactory.githubAction(it)::class }
        }
    }
}