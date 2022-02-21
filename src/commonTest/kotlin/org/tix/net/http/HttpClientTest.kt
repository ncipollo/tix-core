package org.tix.net.http

import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import org.tix.test.runTestWorkaround
import kotlin.test.Test
import kotlin.test.expect

class HttpClientTest {
    @Serializable
    data class TestResponse(val status: String)

    @Test
    fun testHttp() = runTestWorkaround {
        val client = httpClient()
        val response = client.get<TestResponse>("https://cors-test.appspot.com/test")
        expect("ok") { response.status }
    }
}