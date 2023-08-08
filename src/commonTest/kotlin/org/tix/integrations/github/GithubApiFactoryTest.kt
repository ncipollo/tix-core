package org.tix.integrations.github

import org.tix.fixture.config.mockGithubConfig
import kotlin.test.Test
import kotlin.test.assertNotNull

class GithubApiFactoryTest {
    private val factory = GithubApiFactory()

    @Test
    fun api() {
        assertNotNull(factory.api(mockGithubConfig))
    }
}