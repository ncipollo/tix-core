package org.tix.net.http

import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import org.tix.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.expect

class HttpClientTest {
    private val client = httpClient()

    @Serializable
    data class TestResponse(val status: String)

    @Test
    fun testHttp() = runBlockingTest {
        val response = client.get<TestResponse>("https://cors-test.appspot.com/test")
        expect("ok") { response.status }
    }
}