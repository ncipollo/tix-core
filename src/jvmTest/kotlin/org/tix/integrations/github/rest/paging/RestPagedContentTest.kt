package org.tix.integrations.github.rest.paging

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.reflect.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.expect

class RestPagedContentTest {
    private val bodyContent = listOf("item_1", "item_2")
    private val nextLink = "https://api.github.com/repositories/1300192/issues?per_page=2&page=2"

    @Test
    fun pagedContent_withNextLink() = runTest {
        val httpResponse = mockk<HttpResponse> {
            coEvery { body<List<String>>(typeInfo<List<String>>()) } returns bodyContent
            every { headers } returns headersOf("link", "<$nextLink>; rel=\"next\"")
        }

        val expected = RestPagedContent(items = bodyContent, nextLink = nextLink)
        expect(expected) {
            httpResponse.pagedContent()
        }
    }

    @Test
    fun pagedContent_withoutNextLink() = runTest {
        val httpResponse = mockk<HttpResponse> {
            coEvery { body<List<String>>(typeInfo<List<String>>()) } returns bodyContent
            every { headers } returns headersOf()
        }

        val expected = RestPagedContent(items = bodyContent)
        expect(expected) {
            httpResponse.pagedContent()
        }
    }
}