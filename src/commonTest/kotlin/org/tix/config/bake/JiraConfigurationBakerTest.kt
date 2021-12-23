package org.tix.config.bake

import org.tix.config.bake.validation.ConfigValidationException
import org.tix.config.data.JiraConfiguration
import org.tix.config.data.TicketWorkflows
import org.tix.config.data.raw.RawJiraConfiguration
import org.tix.fixture.config.authConfiguration
import org.tix.fixture.config.rawJiraConfiguration
import org.tix.fixture.config.workflows
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.expect

class JiraConfigurationBakerTest {
    @Test
    fun bake_returnsBakedConfiguration_whenConfigHasAllProperties() {
        val config = rawJiraConfiguration
        val expectedConfig = JiraConfiguration(
            auth = authConfiguration,
            noEpics = config.noEpics!!,
            fields = config.fields,
            url = config.url!!,
            workflows = workflows
        )
        expect(expectedConfig) { JiraConfigurationBaker.bake(config, authConfiguration) }
    }

    @Test
    fun bake_returnsConfigWithDefaults_whenFieldsAreMissing() {
        val config = RawJiraConfiguration(url = "url")
        val expectedConfig = JiraConfiguration(
            auth = authConfiguration,
            noEpics = false,
            fields = config.fields,
            url = config.url!!,
            workflows = TicketWorkflows()
        )
        expect(expectedConfig) { JiraConfigurationBaker.bake(config, authConfiguration) }
    }

    @Test
    fun bake_throws_whenConfigIsInvalid() {
        val config = RawJiraConfiguration()
        assertFailsWith(ConfigValidationException::class) { JiraConfigurationBaker.bake(config, authConfiguration) }
    }
}