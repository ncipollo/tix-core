package org.tix.config.data

import org.tix.fixture.config.githubConfig
import kotlin.test.Test
import kotlin.test.expect

class GithubConfigurationTest {
    @Test
    fun startingLevel_whenProjectsAreDisabled() {
        val config = githubConfig.copy(noProjects = true)
        expect(1) { config.startingLevel }
    }

    @Test
    fun startingLevel_whenProjectsAreEnabled() {
        val config = githubConfig.copy(noProjects = false)
        expect(0) { config.startingLevel }
    }

    @Test
    fun fieldsForLevel() {
        val expected = mapOf(
            "common" to "project",
            "unique0" to "default",
            "unique1" to "project",
        )
        expect(expected) { githubConfig.fieldsForLevel(0) }
    }
}