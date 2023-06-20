package org.tix.integrations.github.graphql.project

import kotlinx.serialization.Serializable
import org.tix.config.data.GithubConfiguration
import org.tix.integrations.github.graphql.GithubQueryApi
import org.tix.integrations.github.graphql.query
import org.tix.integrations.github.graphql.response.GithubQueryResponse
import org.tix.integrations.github.graphql.response.ResponseContent
import org.tix.integrations.shared.graphql.escapeTripleQuotes
import org.tix.integrations.shared.graphql.quote

class ProjectQueries(config: GithubConfiguration, private val queryApi: GithubQueryApi) {
    private val owner = config.owner
    private val repo = config.repo

    private companion object {
        const val PROJECT_PROPERTIES = """
            id
            number
            title
            shortDescription
            closed
        """
    }

    suspend fun createProject(
        repoId: String,
        ownerId: String,
        title: String
    ): GithubQueryResponse<CreateProjectResult> {
        val mutation = """
            mutation CreateProject {
              createProjectV2(
                input: {repositoryId: "$repoId", ownerId: "$ownerId", title: "$title"}
              ) {
                content: projectV2 {
                    $PROJECT_PROPERTIES
                }
              }
            }
        """.trimIndent()

        return queryApi.query(mutation)
    }


    suspend fun deleteProject(projectId: String): GithubQueryResponse<DeleteProjectResult> {
        val mutation = """
            mutation DeleteProject {
              deleteProjectV2(input: {projectId: "$projectId"}) {
                content: projectV2 {
                    $PROJECT_PROPERTIES
                }
              }
            }
        """.trimIndent()
        return queryApi.query(mutation)
    }

    suspend fun updateProject(
        projectId: String,
        title: String,
        description: String,
        close: Boolean = false
    ): GithubQueryResponse<UpdateProjectResult> {
        val closeStr = close.toString().lowercase()
        val mutation = """
            mutation UpdateProject {
              updateProjectV2(
                input: {projectId: "$projectId",
                        title: "$title",
                        shortDescription: "${description.escapeTripleQuotes()}",
                        closed: $closeStr}
              ) {
                content: projectV2 {
                    $PROJECT_PROPERTIES
                }
              }
            }
        """.trimIndent()
        return queryApi.query(mutation)
    }

    suspend fun addItemToProject(projectId: String, itemId: String): GithubQueryResponse<AddItemToProjectResult> {
        val mutation = """
            mutation AddItemToProject {
              addProjectV2ItemById(input: {projectId: "$projectId", contentId: "$itemId"}) {
                content: item {
                  id
                }
              }
            }
        """.trimIndent()

        return queryApi.query(mutation)
    }

    suspend fun repoProjectItems(
        projectNumber: Long,
        cursor: String? = null,
        first: Int = 50
    ): GithubQueryResponse<ProjectItemsResponse> {
        val quotedCursor = cursor.quote()
        val assigneeProperties = """
            id
            login
            name
        """.trimIndent()
        val query = """
            query RepoProjectIssues {
              repository(owner: "$owner", name: "$repo") {
                projectV2(number: $projectNumber) {
                  content: items(first: $first, after: $quotedCursor) {
                    nodes {
                      item: content {
                        ... on DraftIssue {
                          id
                          title
                          body
                          assignees(first: 20) {
                            nodes {
                              $assigneeProperties
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
                              $assigneeProperties
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
                              $assigneeProperties
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
        return queryApi.query(query)
    }
}

@Serializable
data class AddItemToProjectResult(val addProjectV2ItemById: ResponseContent<ItemIdNode>)

@Serializable
data class CreateProjectResult(val createProjectV2: ResponseContent<ProjectV2Node>)

@Serializable
data class DeleteProjectResult(val deleteProjectV2: ResponseContent<ProjectV2Node>)

@Serializable
data class UpdateProjectResult(val updateProjectV2: ResponseContent<ProjectV2Node>)