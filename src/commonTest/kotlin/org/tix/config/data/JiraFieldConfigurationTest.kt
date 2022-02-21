package org.tix.config.data

import org.tix.fixture.config.jiraFieldConfig
import kotlin.test.Test
import kotlin.test.expect

class JiraFieldConfigurationTest {
    @Test
    fun forLevel_whenLevelIs0_returnsEpicMergedIntoDefaults() {
        val expected = mapOf(
            "common" to "epic",
            "unique0" to "default",
            "unique1" to "epic",
        )
        expect(expected) { jiraFieldConfig.forLevel(0) }
    }

    @Test
    fun forLevel_whenLevelIs1_returnsIssueMergedIntoDefaults() {
        val expected = mapOf(
            "common" to "issue",
            "unique0" to "default",
            "unique2" to "issue",
        )
        expect(expected) { jiraFieldConfig.forLevel(1) }
    }

    @Test
    fun forLevel_whenLevelIs2_returnsTaskMergedIntoDefaults() {
        val expected = mapOf(
            "common" to "task",
            "unique0" to "default",
            "unique3" to "task",
        )
        expect(expected) { jiraFieldConfig.forLevel(2) }
    }

    @Test
    fun forLevel_whenLevelIsInvalid_returnsOnlyDefaults() {
        expect(jiraFieldConfig.default) { jiraFieldConfig.forLevel(3) }
    }
}