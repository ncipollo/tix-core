package org.tix.integrations.github.rest.issue

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.tix.integrations.github.GithubUrls
import org.tix.integrations.github.rest.paging.RestPagedContent
import org.tix.integrations.github.rest.paging.pagedContent
import org.tix.integrations.github.rest.sort.SortDirection
import org.tix.integrations.github.state.State
import org.tix.integrations.github.state.StateQuery
import org.tix.integrations.github.state.StateReason

class IssueApi(private val urls: GithubUrls, private val client: HttpClient) {
    suspend fun create(createRequest: IssueCreateRequest): Issue {
        val url = urls.repos.withPath("issues")
        return client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(createRequest)
        }.body()
    }

    suspend fun get(issueNumber: Long): Issue {
        val url = urls.repos.withPath("issues/${issueNumber}")
        return client.get(url).body()
    }

    suspend fun repoIssues(
        milestone: String? = null,
        state: StateQuery? = null,
        assignee: String? = null,
        creator: String? = null,
        mentioned: String? = null,
        sort: IssueSort? = null,
        direction: SortDirection? = null
    ): RestPagedContent<Issue> {
        val url = urls.repos.withPath("issues")
        return client.get(url) {
            milestone?.let { parameter("milestone", it) }
            state?.let { parameter("state", it.name.lowercase()) }
            assignee?.let { parameter("assignee", it) }
            creator?.let { parameter("creator", it) }
            mentioned?.let { parameter("mentioned", it) }
            sort?.let { parameter("sort", it.name.lowercase()) }
            direction?.let { parameter("direction", it.name.lowercase()) }
        }.pagedContent()
    }

    suspend fun update(issueNumber: Long, updateRequest: IssueUpdateRequest): Issue {
        val url = urls.repos.withPath("issues/$issueNumber")
        return client.patch(url) {
            contentType(ContentType.Application.Json)
            setBody(updateRequest)
        }.body()
    }

    suspend fun closeIssue(issueNumber: Long, stateReason: StateReason = StateReason.COMPLETED) =
        update(issueNumber, IssueUpdateRequest(state = State.CLOSED, stateReason = stateReason))
}