package org.tix.integrations.github

import kotlinx.coroutines.test.runTest
import org.tix.fixture.integrations.githubApi
import org.tix.integrations.github.rest.milestone.MilestoneCreateRequest
import org.tix.integrations.github.rest.paging.GithubRestPageFetcher
import org.tix.integrations.github.state.StateQuery
import kotlin.test.Ignore
import kotlin.test.Test


// Comment out ignore to test Github API IRL
@Ignore
class GithubApiTest {
    private val githubApi = githubApi()
    private val pager = GithubRestPageFetcher(githubApi.rest.paging)

    @Test
    fun milestones() = runTest {
        val result = githubApi.rest.milestones.create(
            MilestoneCreateRequest(
                title = "Test API Milestone",
                description = "Created by rest API"
            )
        )
        val milestones = pager.fetchAllPages { githubApi.rest.milestones.repoMilestones(StateQuery.ALL) }
        milestones.forEach {
            print("- ${it.title}")
        }
        githubApi.rest.milestones.delete(result.number)
    }

    @Test
    fun repo() = runTest {
        val repo = githubApi.rest.repos.get()
        println("Repo - $repo")
    }

    @Test
    fun project() = runTest {
        val repo = githubApi.rest.repos.get()
        val project = githubApi.queries.projects.createProject(
            repoId = repo.nodeId,
            ownerId = repo.owner.nodeId,
            title = "Test Project"
        ).data.createProjectV2.content

        println("Project: $project")

        try {
            val updatedProject = githubApi.queries.projects.updateProject(
                projectId = project.id,
                title = "Test Project",
                description = "This is a test",
                close = false
            )

            println("Update Result: $updatedProject")
        } catch (_: Throwable) {

        }

        val addItemResult = githubApi.queries.projects.addItemToProject(
            projectId = project.id,
            itemId = "I_kwDOFBT-qs5p88KY"
        )
        println("Add item Result: $addItemResult")

        val deleteResult = githubApi.queries.projects.deleteProject(projectId = project.id)

        println("Delete Result: $deleteResult")

    }

    @Test
    fun repoProject() = runTest {
        val project = githubApi.queries.projects.repoProject(1).data.repository.projectV2
        println(project)
    }

    @Test
    fun projectItems() = runTest {
        val items = githubApi.queries.projects.repoProjectItems(1).data.repository.projectV2.content.nodes
        println("Items: $items")
    }
}