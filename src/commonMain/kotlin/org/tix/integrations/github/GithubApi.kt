package org.tix.integrations.github

import io.ktor.client.engine.*
import org.tix.config.data.GithubConfiguration
import org.tix.integrations.github.graphql.GithubQueries
import org.tix.integrations.github.rest.GithubRestApi

class GithubApi(config: GithubConfiguration, engine: HttpClientEngine? = null) {
    val queries = GithubQueries(config, engine)
    val rest = GithubRestApi(config, engine)
}