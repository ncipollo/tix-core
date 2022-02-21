package org.tix.config.bake

import org.tix.config.data.GithubFieldConfiguration
import org.tix.config.data.raw.RawGithubFieldConfiguration
import org.tix.fixture.config.githubFieldConfig
import org.tix.fixture.config.rawGithubFieldConfig
import kotlin.test.Test
import kotlin.test.expect

class GithubConfigurationFieldBakerTests {
    @Test
    fun bake_emptyRawConfig() {
        expect(GithubFieldConfiguration()) { GithubConfigurationFieldBaker.bake(RawGithubFieldConfiguration()) }
    }

    @Test
    fun bake_populatedRawConfig() {
        expect(githubFieldConfig) { GithubConfigurationFieldBaker.bake(rawGithubFieldConfig) }
    }
}