package org.tix.config.bake

import org.tix.config.bake.validation.ConfigValidationException
import org.tix.config.data.GithubConfiguration
import org.tix.config.data.TicketWorkflows
import org.tix.config.data.raw.RawGithubConfiguration
import org.tix.fixture.config.authConfiguration
import org.tix.fixture.config.rawGithubConfiguration
import org.tix.fixture.config.workflows
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.expect

class GithubConfigurationBakerTest {
    @Test
    fun bake_returnsBakedConfiguration_whenConfigHasAllProperties() {
        val config = rawGithubConfiguration
        val expectedConfig = GithubConfiguration(
            auth = authConfiguration,
            owner = config.owner!!,
            repo = config.repo!!,
            noProjects = config.noProjects!!,
            fields = GithubConfigurationFieldBaker.bake(config.fields),
            workflows = workflows
        )
        expect(expectedConfig) { GithubConfigurationBaker.bake(config, authConfiguration) }
    }

    @Test
    fun bake_returnsConfigWithDefaults_whenFieldsAreMissing() {
        val config = RawGithubConfiguration(owner = "owner", repo = "repo")
        val expectedConfig = GithubConfiguration(
            auth = authConfiguration,
            owner = config.owner!!,
            repo = config.repo!!,
            noProjects = false,
            fields = GithubConfigurationFieldBaker.bake(config.fields),
            workflows = TicketWorkflows()
        )
        expect(expectedConfig) { GithubConfigurationBaker.bake(config, authConfiguration) }
    }

    @Test
    fun bake_throws_whenConfigIsInvalid() {
        val config = RawGithubConfiguration()
        assertFailsWith(ConfigValidationException::class) { GithubConfigurationBaker.bake(config, authConfiguration) }
    }
}