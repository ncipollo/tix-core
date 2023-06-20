package org.tix.integrations.github

import org.tix.fixture.config.mockGithubConfig
import org.tix.net.BaseUrl
import kotlin.test.Test
import kotlin.test.expect

class GithubUrlsTest {
    private val urls = GithubUrls(mockGithubConfig)

    @Test
    fun repos() {
        expect(BaseUrl("https://api.github.com/repos/owner/repo")) { urls.repos }
    }
}