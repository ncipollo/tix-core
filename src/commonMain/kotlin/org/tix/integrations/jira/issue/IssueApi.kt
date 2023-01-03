package org.tix.integrations.jira.issue

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.tix.net.BaseUrl

class IssueApi(private val baseUrl: BaseUrl, private val client: HttpClient) {
    suspend fun create(issue: Issue): Issue {
        val url = baseUrl.withPath("rest/api/2/issue")
        return client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(issue)
        }.body()
    }

    suspend fun delete(issueId: String) {
        val url = baseUrl.withPath("rest/api/2/issue/${issueId}")
        client.delete(url) {
            url {
                parameters.append("deleteSubtasks", "true")
            }
        }
    }

    suspend fun get(issueId: String): Issue {
        val url = baseUrl.withPath("rest/api/2/issue/${issueId}")
        return client.get(url).body()
    }

    suspend fun search(jql: String): List<Issue> {
        val url = baseUrl.withPath("rest/api/2/search")
        val result =  client.get(url) {
            url {
                parameters.append("jql", jql)
            }
        }.body<SearchResult>()
        return result.issues
    }

    suspend fun update(issue: Issue): Issue {
        val url = baseUrl.withPath("rest/api/2/issue/${issue.keyOrId}")
        client.put(url) {
            contentType(ContentType.Application.Json)
            setBody(issue)
        }
        return issue
    }

    @Serializable
    data class SearchResult(val issues: List<Issue>)
}