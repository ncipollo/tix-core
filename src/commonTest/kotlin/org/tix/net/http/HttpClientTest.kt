package org.tix.net.http

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import org.tix.test.runTestWorkaround
import kotlin.test.Test
import kotlin.test.expect

class HttpClientTest {
    @Serializable
    data class TestPage(val name: String)

    @Serializable
    data class TestResponse(val page: TestPage)

    @Test
    fun testHttp() = runTestWorkaround {
        val client = httpClient {
            installContentNegotiation()
        }
        val response = client.get("https://www.githubstatus.com/api/v2/summary.json").body<TestResponse>()
        expect("github") { response.page.name.lowercase() }
    }
}