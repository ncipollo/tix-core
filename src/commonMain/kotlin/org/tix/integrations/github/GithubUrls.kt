package org.tix.integrations.github

import org.tix.config.data.GithubConfiguration
import org.tix.net.BaseUrl

class GithubUrls(configuration: GithubConfiguration) {
    private val owner = configuration.owner
    private val repo = configuration.repo

    val repos = BaseUrl("https://api.github.com/repos/$owner/$repo")
}