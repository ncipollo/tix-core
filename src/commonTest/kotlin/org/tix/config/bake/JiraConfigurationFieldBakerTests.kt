package org.tix.config.bake

import org.tix.config.data.JiraFieldConfiguration
import org.tix.config.data.raw.RawJiraFieldConfiguration
import org.tix.fixture.config.jiraFieldConfig
import org.tix.fixture.config.rawJiraFieldConfig
import kotlin.test.Test
import kotlin.test.expect

class JiraConfigurationFieldBakerTests {
    @Test
    fun bake_emptyRawConfig() {
        expect(JiraFieldConfiguration()) { JiraConfigurationFieldBaker.bake(RawJiraFieldConfiguration()) }
    }

    @Test
    fun bake_populatedRawConfig() {
        expect(jiraFieldConfig) { JiraConfigurationFieldBaker.bake(rawJiraFieldConfig) }
    }
}