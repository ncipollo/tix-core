package org.tix.config.data

import org.tix.fixture.config.githubFieldConfig
import kotlin.test.Test
import kotlin.test.expect

class GithubFieldConfigurationTest {
    @Test
    fun forLevel_whenLevelIs0_returnsProjectMergedIntoDefaults() {
        val expected = mapOf(
            "common" to "project",
            "unique0" to "default",
            "unique1" to "project",
        )
        expect(expected) { githubFieldConfig.forLevel(0) }
    }

    @Test
    fun forLevel_whenLevelIs1_returnsIssueMergedIntoDefaults() {
        val expected = mapOf(
            "common" to "issue",
            "unique0" to "default",
            "unique2" to "issue",
        )
        expect(expected) { githubFieldConfig.forLevel(1) }
    }

    @Test
    fun forLevel_whenLevelIsInvalid_returnsOnlyDefaults() {
        expect(githubFieldConfig.default) { githubFieldConfig.forLevel(100) }
    }
}