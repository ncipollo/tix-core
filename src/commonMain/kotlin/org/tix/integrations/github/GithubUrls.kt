package org.tix.integrations.github

import org.tix.config.data.GithubConfiguration
import org.tix.net.BaseUrl

class GithubUrls(config: GithubConfiguration) {
    private val owner = config.owner
    private val repo = config.repo

    val repos = BaseUrl("https://api.github.com/repos/$owner/$repo")
}