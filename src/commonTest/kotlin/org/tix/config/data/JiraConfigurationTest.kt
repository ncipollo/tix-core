package org.tix.config.data

import org.tix.fixture.config.mockJiraConfig
import kotlin.test.Test
import kotlin.test.expect

class JiraConfigurationTest {
    @Test
    fun startingLevel_whenEpicsAreDisabled() {
        val config = mockJiraConfig.copy(noEpics = true)
        expect(1) { config.startingLevel }
    }

    @Test
    fun startingLevel_whenEpicsAreEnabled() {
        val config = mockJiraConfig.copy(noEpics = false)
        expect(0) { config.startingLevel }
    }

    @Test
    fun fieldsForLevel() {
        val expected = mapOf(
            "common" to "epic",
            "unique0" to "default",
            "unique1" to "epic",
        )
        expect(expected) { mockJiraConfig.fieldsForLevel(0) }
    }
}