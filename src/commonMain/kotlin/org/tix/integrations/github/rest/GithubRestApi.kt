package org.tix.integrations.github.rest

import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.tix.config.data.GithubConfiguration
import org.tix.integrations.github.GithubUrls
import org.tix.integrations.github.rest.issue.IssueApi
import org.tix.integrations.github.rest.milestone.MilestoneApi
import org.tix.integrations.github.rest.paging.PagingApi
import org.tix.integrations.github.rest.repo.RepoApi
import org.tix.net.http.httpClient
import org.tix.net.http.installContentNegotiation

class GithubRestApi(config: GithubConfiguration, engine: HttpClientEngine? = null) {
    private val restClient = httpClient(engine) {
        installContentNegotiation(useSnakeCase = true)
        defaultRequest {
            accept(ContentType("application", "vnd.github+json"))
            bearerAuth(config.auth.password)
            header("X-GitHub-Api-Version", "2022-11-28")
        }
    }
    private val urls = GithubUrls(config)

    val issues = IssueApi(urls, restClient)
    val milestones = MilestoneApi(urls, restClient)
    val paging = PagingApi(restClient)
    val repos = RepoApi(urls, restClient)
}