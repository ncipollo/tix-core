package org.tix.integrations.github.rest.milestone

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import org.tix.fixture.integrations.githubConfig
import org.tix.fixture.integrations.mockGithubApi
import org.tix.integrations.github.GithubUrls
import org.tix.integrations.github.rest.paging.RestPagedContent
import org.tix.integrations.github.state.StateQuery
import org.tix.net.http.httpJson
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.expect

class MilestoneApiTest {
    private val json = httpJson(useSnakeCase = true)
    private val urls = GithubUrls(githubConfig())

    @Test
    fun create() = runTest {
        val createRequest = MilestoneCreateRequest(
            title = "title",
            description = "description"
        )
        val milestoneResult = MilestoneCreateResponse(number = 1)
        val mockEngine = MockEngine { request ->
            val textBody = request.body as TextContent

            assertEquals(json.encodeToString(createRequest), textBody.text)
            assertEquals(request.url.toString(), urls.repos.withPath("milestones"))

            respond(
                content = json.encodeToString(milestoneResult),
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val milestoneApi = mockGithubApi(mockEngine).rest.milestones
        expect(milestoneResult) {
            milestoneApi.create(createRequest)
        }
    }

    @Test
    fun delete() = runTest {
        val resultMilestone = Milestone(number = 1)
        val mockEngine = MockEngine { request ->
            assertEquals(urls.repos.withPath("milestones/1"), request.url.toString())
            assertEquals(HttpMethod.Delete, request.method)
            respond(
                content = json.encodeToString(resultMilestone),
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val milestoneApi = mockGithubApi(mockEngine).rest.milestones
        milestoneApi.delete(1)
    }

    @Test
    fun repoMilestones_defaults() = runTest {
        val resultMilestone = Milestone(number = 1)
        val mockEngine = MockEngine { request ->
            assertEquals(urls.repos.withPath("milestones"), request.url.toString())
            respond(
                content = json.encodeToString(listOf(resultMilestone)),
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val milestoneApi = mockGithubApi(mockEngine).rest.milestones
        val pagedContent = RestPagedContent(items = listOf(resultMilestone))
        expect(pagedContent) {
            milestoneApi.repoMilestones()
        }
    }

    @Test
    fun repoMilestones_parametersProvided() = runTest {
        val resultMilestone = Milestone(number = 1)
        val mockEngine = MockEngine { request ->
            val expectedParameters = parametersOf(
                "state" to listOf("closed")
            )
            assertEquals(expectedParameters, request.url.parameters)
            respond(
                content = json.encodeToString(listOf(resultMilestone)),
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val milestoneApi = mockGithubApi(mockEngine).rest.milestones
        val pagedContent = RestPagedContent(items = listOf(resultMilestone))
        expect(pagedContent) {
            milestoneApi.repoMilestones(state = StateQuery.CLOSED)
        }
    }


}