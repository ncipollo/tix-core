package org.tix.integrations.github.graphql

import io.ktor.client.engine.*
import org.tix.config.data.GithubConfiguration
import org.tix.integrations.github.graphql.project.ProjectQueries

class GithubQueries(config: GithubConfiguration, engine: HttpClientEngine? = null) {
    private val api = GithubGraphqlApi(config, engine)

    val projects = ProjectQueries(config, api)
}