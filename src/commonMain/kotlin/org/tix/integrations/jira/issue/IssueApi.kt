package org.tix.integrations.jira.issue

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.tix.net.BaseUrl

class IssueApi(private val baseUrl: BaseUrl, private val client: HttpClient) {
    suspend fun create(issue: Issue): Issue {
        val url = baseUrl.withPath("rest/api/2/issue")
        return client.post(url) {
            contentType(ContentType.Application.Json)
            body = issue
        }
    }

    suspend fun delete(issueId: String) {
        val url = baseUrl.withPath("rest/api/2/issue/${issueId}")
        client.delete<Unit>(url) {
            parameter("deleteSubtasks", "true")
        }
    }

    suspend fun get(issueId: String): Issue {
        val url = baseUrl.withPath("rest/api/2/issue/${issueId}")
        return client.get(url)
    }

    suspend fun search(jql: String): List<Issue> {
        val url = baseUrl.withPath("rest/api/2/search")
        val result =  client.get<SearchResult>(url) {
            parameter("jql", jql)
        }
        return result.issues
    }

    suspend fun update(issue: Issue): Issue {
        val url = baseUrl.withPath("rest/api/2/issue/${issue.keyOrId}")
        client.put<Unit>(url) {
            contentType(ContentType.Application.Json)
            body = issue
        }
        return issue
    }

    @Serializable
    data class SearchResult(val issues: List<Issue>)
}