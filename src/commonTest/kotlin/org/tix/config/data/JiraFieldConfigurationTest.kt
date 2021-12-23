package org.tix.config.data

import org.tix.fixture.config.jiraFieldConfig
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.Test
import kotlin.test.expect

class JiraFieldConfigurationTest {
    @Test
    fun forLevel_whenLevelIs0_returnsEpicMergedIntoDefaults() {
        val expected = mapOf(
            "common" to DynamicElement("epic"),
            "unique0" to DynamicElement("default"),
            "unique1" to DynamicElement("epic"),
        )
        expect(expected) { jiraFieldConfig.forLevel(0) }
    }

    @Test
    fun forLevel_whenLevelIs1_returnsIssueMergedIntoDefaults() {
        val expected = mapOf(
            "common" to DynamicElement("issue"),
            "unique0" to DynamicElement("default"),
            "unique2" to DynamicElement("issue"),
        )
        expect(expected) { jiraFieldConfig.forLevel(1) }
    }

    @Test
    fun forLevel_whenLevelIs2_returnsTaskMergedIntoDefaults() {
        val expected = mapOf(
            "common" to DynamicElement("task"),
            "unique0" to DynamicElement("default"),
            "unique3" to DynamicElement("task"),
        )
        expect(expected) { jiraFieldConfig.forLevel(2) }
    }

    @Test
    fun forLevel_whenLevelIsInvalid_returnsOnlyDefaults() {
        expect(jiraFieldConfig.default) { jiraFieldConfig.forLevel(3) }
    }
}