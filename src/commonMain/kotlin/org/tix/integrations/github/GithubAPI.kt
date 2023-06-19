package org.tix.integrations.github

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.tix.config.data.GithubConfiguration
import org.tix.net.http.httpClient

class GithubAPI(configuration: GithubConfiguration) {
    private val client = httpClient {
        defaultRequest {
            accept(ContentType("application", "vnd.github+json"))
            bearerAuth(configuration.auth.password)
        }
    }
    private val urls = GithubUrls(configuration)
}