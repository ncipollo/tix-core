package org.tix.integrations.github.rest.repo

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import org.tix.fixture.integrations.githubConfig
import org.tix.fixture.integrations.mockGithubApi
import org.tix.integrations.github.GithubUrls
import org.tix.net.http.httpJson
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.expect

class RepoApiTest {
    private val json = httpJson(useSnakeCase = true)
    private val urls = GithubUrls(githubConfig())

    @Test
    fun get() = runTest {
        val resultRepo = Repository(name = "test", fullName = "owner/test")
        val mockEngine = MockEngine { request ->
            assertEquals(urls.repos.toString(), request.url.toString())
            respond(
                content = json.encodeToString(resultRepo),
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val repoApi = mockGithubApi(mockEngine).rest.repos
        expect(resultRepo) {
            repoApi.get()
        }
    }
}