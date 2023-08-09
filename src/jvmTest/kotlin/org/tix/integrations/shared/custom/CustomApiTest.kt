package org.tix.integrations.shared.custom

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Ignore
import org.junit.Test
import org.tix.fixture.integrations.jiraApi
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.expect

@Ignore
class CustomApiTest {
    private companion object {
        val ISSUE_MAP = mapOf(
            "fields" to mapOf(
                "summary" to "Custom request ticket",
                "description" to "Created with custom request",
                "project" to mapOf("key" to "TIX"),
                "issuetype" to mapOf("name" to "Story")
            )
        )
    }

    private val customApi = jiraApi().custom
    private val issueApi = jiraApi().issue

    @Test
    fun request_withBody() = runTest {
        val request = CustomRequest(
            method = CustomRequestMethod.POST,
            path = "rest/api/2/issue",
            body = DynamicElement(ISSUE_MAP)
        )

        val response = customApi.request(request)
        val responseBody = response.asMap()
        val key = responseBody["key"] as? String

        expect(true) { key != null }
        issueApi.delete(key!!)
    }

    @Test
    fun request_withParameters() = runBlocking {
        val request = CustomRequest(
            method = CustomRequestMethod.GET,
            path = "rest/api/2/search",
            parameters = mapOf("jql" to "summary ~ \"first\"")
        )

        val response = customApi.request(request)
        val responseBody = response.asMap()

        expect(1L) { responseBody["total"] }
    }
}