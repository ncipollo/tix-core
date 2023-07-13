package org.tix.integrations.github.rest.milestone

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.tix.integrations.github.GithubUrls
import org.tix.integrations.github.rest.paging.RestPagedContent
import org.tix.integrations.github.rest.paging.pagedContent
import org.tix.integrations.github.state.StateQuery

class MilestoneApi(private val urls: GithubUrls, private val client: HttpClient) {
    suspend fun create(createRequest: MilestoneCreateRequest): Milestone {
        val url = urls.repos.withPath("milestones")
        return client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(createRequest)
        }.body()
    }

    suspend fun repoMilestones(state: StateQuery? = null): RestPagedContent<Milestone> {
        val url = urls.repos.withPath("milestones")
        return client.get(url) {
            state?.let { parameter("state", it.name.lowercase()) }
        }.pagedContent()
    }

    suspend fun delete(milestoneNumber: Long) {
        val url = urls.repos.withPath("milestones/$milestoneNumber")
        client.delete(url)
    }
}