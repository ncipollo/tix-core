package org.tix.integrations.github

import io.ktor.client.engine.*
import org.tix.config.data.GithubConfiguration

class GithubApiFactory(private val engine: HttpClientEngine? = null) {
    fun api(config: GithubConfiguration) = GithubApi(config, engine)
}