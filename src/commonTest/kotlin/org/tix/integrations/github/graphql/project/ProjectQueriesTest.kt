package org.tix.integrations.github.graphql.project

import kotlinx.coroutines.test.runTest
import org.tix.fixture.config.mockGithubConfig
import org.tix.integrations.github.graphql.TestGithubQueryApi
import org.tix.integrations.github.graphql.paging.QueryPagedContent
import org.tix.integrations.github.graphql.repository.PagedRepository
import org.tix.integrations.github.graphql.response.GithubQueryResponse
import org.tix.integrations.github.graphql.response.ResponseContent
import kotlin.test.Test
import kotlin.test.assertEquals

class ProjectQueriesTest {
    private val config = mockGithubConfig
    private val queryApi = TestGithubQueryApi()
    private val projectQueries = ProjectQueries(config, queryApi)

    @Test
    fun createProject() = runTest {
        val expectedResponse = GithubQueryResponse(CreateProjectResult(ResponseContent(ProjectV2Node(id = "id"))))
        queryApi.prepareResponse(expectedResponse)

        val response = projectQueries.createProject(repoId = "repoId", ownerId = "ownerId", title = "title")
        assertEquals(expectedResponse, response)
        queryApi.assertReturnType<GithubQueryResponse<CreateProjectResult>>()
        queryApi.assertQuery(
            """
            mutation CreateProject {
              createProjectV2(
                input: {repositoryId: "repoId", ownerId: "ownerId", title: "title"}
              ) {
                content: projectV2 {
                    
            id
            number
            title
            shortDescription
            closed

                }
              }
            }
        """.trimIndent()
        )
    }

    @Test
    fun deleteProject() = runTest {
        val expectedResponse = GithubQueryResponse(DeleteProjectResult(ResponseContent(ProjectV2Node(id = "id"))))
        queryApi.prepareResponse(expectedResponse)

        val response = projectQueries.deleteProject(projectId = "id")
        assertEquals(expectedResponse, response)
        queryApi.assertReturnType<GithubQueryResponse<DeleteProjectResult>>()
        queryApi.assertQuery(
            """
            mutation DeleteProject {
              deleteProjectV2(input: {projectId: "id"}) {
                content: projectV2 {
                    
            id
            number
            title
            shortDescription
            closed

                }
              }
            }
        """.trimIndent()
        )
    }

    @Test
    fun updateProject() = runTest {
        val expectedResponse = GithubQueryResponse(UpdateProjectResult(ResponseContent(ProjectV2Node(id = "id"))))
        queryApi.prepareResponse(expectedResponse)

        val response = projectQueries.updateProject(projectId = "id", title = "title", description = "description")
        assertEquals(expectedResponse, response)
        queryApi.assertReturnType<GithubQueryResponse<UpdateProjectResult>>()
        queryApi.assertQuery(
            """
            mutation UpdateProject {
              updateProjectV2(
                input: {projectId: "id",
                        title: "title",
                        shortDescription: "description",
                        closed: false}
              ) {
                content: projectV2 {
                    
            id
            number
            title
            shortDescription
            closed

                }
              }
            }
        """.trimIndent()
        )
    }

    @Test
    fun addItemToProject() = runTest {
        val expectedResponse = GithubQueryResponse(AddItemToProjectResult(ResponseContent(ItemIdNode(id = "id"))))
        queryApi.prepareResponse(expectedResponse)

        val response = projectQueries.addItemToProject(projectId = "id", itemId = "itemId")
        assertEquals(expectedResponse, response)
        queryApi.assertReturnType<GithubQueryResponse<AddItemToProjectResult>>()
        queryApi.assertQuery(
            """
            mutation AddItemToProject {
              addProjectV2ItemById(input: {projectId: "id", contentId: "itemId"}) {
                content: item {
                  id
                }
              }
            }
        """.trimIndent()
        )
    }

    @Test
    fun repoProject() = runTest {
        val expectedResponse = GithubQueryResponse(ProjectResponse(ProjectV2Wrapper(ProjectV2Node(number = 1))))
        queryApi.prepareResponse(expectedResponse)

        val response = projectQueries.repoProject(1)
        assertEquals(expectedResponse, response)
        queryApi.assertReturnType<GithubQueryResponse<ProjectResponse>>()
        queryApi.assertQuery(
            """
                query GetProject {
                  repository(owner: "owner", name: "repo") {
                    projectV2(number: 1) {
                      id
                      number
                      title
                      shortDescription
                      closed
                    }
                  }
                }
            """.trimIndent()
        )
    }

    @Test
    fun repoProjectItems() = runTest {
        val expectedResponse = GithubQueryResponse(
            ProjectItemsResponse(
                PagedRepositoryProjectItems(
                    PagedRepository(
                        QueryPagedContent(listOf(ProjectItemWrapper(item = ProjectDraftIssueNode(id = "id"))))
                    )
                )
            )
        )
        queryApi.prepareResponse(expectedResponse)

        val response = projectQueries.repoProjectItems(projectNumber = 1)
        assertEquals(expectedResponse, response)
        queryApi.assertReturnType<GithubQueryResponse<ProjectItemsResponse>>()
        queryApi.assertQuery(
            """
                       query RepoProjectIssues {
                         repository(owner: "owner", name: "repo") {
                           projectV2(number: 1) {
                             content: items(first: 50, after: null) {
                               nodes {
                                 item: content {
                                   ... on DraftIssue {
                                     id
                                     title
                                     body
                                     assignees(first: 20) {
                                       nodes {
                                         id
           login
           name
                                       }
                                     }
                                     itemType: __typename
                                   }
                                   ... on Issue {
                                     id
                                     number
                                     title
                                     body
                                     assignees(first: 20) {
                                       nodes {
                                         id
           login
           name
                                       }
                                     }
                                     state
                                     itemType: __typename
                                   }
                                   ... on PullRequest {
                                     id
                                     number
                                     title
                                     body
                                     assignees(first: 20) {
                                       nodes {
                                         id
           login
           name
                                       }
                                     }
                                     state
                                     itemType: __typename
                                   }
                                 }
                               }
                               pageInfo {
                                 hasNextPage
                                 endCursor
                               }
                             }
                           }
                         }
                       }
        """.trimIndent()
        )
    }
}