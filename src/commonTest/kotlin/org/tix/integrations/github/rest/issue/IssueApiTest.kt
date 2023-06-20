package org.tix.integrations.github.rest.issue

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import org.tix.fixture.integrations.githubConfig
import org.tix.fixture.integrations.mockGithubApi
import org.tix.integrations.github.GithubUrls
import org.tix.integrations.github.rest.paging.RestPagedContent
import org.tix.integrations.github.rest.sort.SortDirection
import org.tix.integrations.github.state.State
import org.tix.integrations.github.state.StateQuery
import org.tix.integrations.github.state.StateReason
import org.tix.net.http.httpJson
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.expect

class IssueApiTest {
    private val json = httpJson(useSnakeCase = true)
    private val urls = GithubUrls(githubConfig())

    @Test
    fun create() = runTest {
        val createRequest = IssueCreateRequest(
            title = "title",
            body = "body",
            assignees = listOf("user"),
            milestone = 1,
            labels = listOf("label")
        )
        val resultIssue = Issue(number = 1)
        val mockEngine = MockEngine { request ->
            val textBody = request.body as TextContent

            assertEquals(json.encodeToString(createRequest), textBody.text)
            assertEquals(urls.repos.withPath("issues"), request.url.toString())

            respond(
                content = json.encodeToString(resultIssue),
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val issueApi = mockGithubApi(mockEngine).rest.issues
        expect(resultIssue) {
            issueApi.create(createRequest)
        }
    }

    @Test
    fun get() = runTest {
        val resultIssue = Issue(number = 1)
        val mockEngine = MockEngine { request ->
            assertEquals(urls.repos.withPath("issues/1"), request.url.toString())
            respond(
                content = json.encodeToString(resultIssue),
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val issueApi = mockGithubApi(mockEngine).rest.issues
        expect(resultIssue) {
            issueApi.get(1)
        }
    }

    @Test
    fun repoIssues_defaults() = runTest {
        val resultIssue = Issue(number = 1)
        val mockEngine = MockEngine { request ->
            assertEquals(urls.repos.withPath("issues"), request.url.toString())
            respond(
                content = json.encodeToString(listOf(resultIssue)),
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val issueApi = mockGithubApi(mockEngine).rest.issues
        val pagedContent = RestPagedContent(items = listOf(resultIssue))
        expect(pagedContent) {
            issueApi.repoIssues()
        }
    }

    @Test
    fun repoIssues_parametersProvided() = runTest {
        val resultIssue = Issue(number = 1)
        val mockEngine = MockEngine { request ->
            val expectedParameters = parametersOf(
                "milestone" to listOf("1"),
                "state" to listOf("all"),
                "assignee" to listOf("assignee"),
                "creator" to listOf("creator"),
                "mentioned" to listOf("7/10/2023"),
                "sort" to listOf("created"),
                "direction" to listOf("asc"),
            )
            assertEquals(expectedParameters, request.url.parameters)
            respond(
                content = json.encodeToString(listOf(resultIssue)),
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val issueApi = mockGithubApi(mockEngine).rest.issues
        val pagedContent = RestPagedContent(items = listOf(resultIssue))
        expect(pagedContent) {
            issueApi.repoIssues(
                milestone = "1",
                state = StateQuery.ALL,
                assignee = "assignee",
                creator = "creator",
                mentioned = "7/10/2023",
                sort = IssueSort.CREATED,
                direction = SortDirection.ASC
            )
        }
    }

    @Test
    fun update() = runTest {
        val updateRequest = IssueUpdateRequest(
            title = "title",
            body = "body",
            assignees = listOf("user"),
            milestone = 1,
            labels = listOf("label"),
            state = State.CLOSED,
            stateReason = StateReason.NOT_PLANNED
        )
        val resultIssue = Issue(number = 1)
        val mockEngine = MockEngine { request ->
            val textBody = request.body as TextContent

            assertEquals(json.encodeToString(updateRequest), textBody.text)
            assertEquals(request.method, HttpMethod.Patch)
            assertEquals(request.url.toString(), urls.repos.withPath("issues/1"))

            respond(
                content = json.encodeToString(resultIssue),
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val issueApi = mockGithubApi(mockEngine).rest.issues
        expect(resultIssue) {
            issueApi.update(1, updateRequest)
        }
    }

    @Test
    fun closeIssue() = runTest {
        val updateRequest = IssueUpdateRequest(
            state = State.CLOSED,
            stateReason = StateReason.COMPLETED
        )
        val resultIssue = Issue(number = 1)
        val mockEngine = MockEngine { request ->
            val textBody = request.body as TextContent

            assertEquals(json.encodeToString(updateRequest), textBody.text)
            assertEquals(request.method, HttpMethod.Patch)
            assertEquals(request.url.toString(), urls.repos.withPath("issues/1"))

            respond(
                content = json.encodeToString(resultIssue),
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val issueApi = mockGithubApi(mockEngine).rest.issues
        expect(resultIssue) {
            issueApi.closeIssue(1)
        }
    }
}