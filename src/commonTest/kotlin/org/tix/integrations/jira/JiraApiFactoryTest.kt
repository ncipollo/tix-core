package org.tix.integrations.jira

import org.tix.fixture.config.mockJiraConfig
import kotlin.test.Test
import kotlin.test.assertNotNull

class JiraApiFactoryTest {
    private val factory = JiraApiFactory()

    @Test
    fun api() {
        assertNotNull(factory.api(mockJiraConfig))
    }
}