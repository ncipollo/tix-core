package org.tix.integrations.github.rest.repo

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.tix.integrations.github.GithubUrls

class RepoApi(private val urls: GithubUrls, private val client: HttpClient) {
    suspend fun get(): Repository {
        val url = urls.repos.toString()
        return client.get(url).body()
    }
}