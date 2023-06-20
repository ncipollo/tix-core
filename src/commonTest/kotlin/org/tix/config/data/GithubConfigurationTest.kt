package org.tix.config.data

import org.tix.fixture.config.mockGithubConfig
import kotlin.test.Test
import kotlin.test.expect

class GithubConfigurationTest {
    @Test
    fun startingLevel_whenProjectsAreDisabled() {
        val config = mockGithubConfig.copy(noProjects = true)
        expect(1) { config.startingLevel }
    }

    @Test
    fun startingLevel_whenProjectsAreEnabled() {
        val config = mockGithubConfig.copy(noProjects = false)
        expect(0) { config.startingLevel }
    }

    @Test
    fun fieldsForLevel() {
        val expected = mapOf(
            "common" to "project",
            "unique0" to "default",
            "unique1" to "project",
        )
        expect(expected) { mockGithubConfig.fieldsForLevel(0) }
    }
}