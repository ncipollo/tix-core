package org.tix.config.data

import org.tix.fixture.config.githubFieldConfig
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.Test
import kotlin.test.expect

class GithubFieldConfigurationTest {
    @Test
    fun forLevel_whenLevelIs0_returnsProjectMergedIntoDefaults() {
        val expected = mapOf(
            "common" to DynamicElement("project"),
            "unique0" to DynamicElement("default"),
            "unique1" to DynamicElement("project"),
        )
        expect(expected) { githubFieldConfig.forLevel(0) }
    }

    @Test
    fun forLevel_whenLevelIs1_returnsIssueMergedIntoDefaults() {
        val expected = mapOf(
            "common" to DynamicElement("issue"),
            "unique0" to DynamicElement("default"),
            "unique2" to DynamicElement("issue"),
        )
        expect(expected) { githubFieldConfig.forLevel(1) }
    }

    @Test
    fun forLevel_whenLevelIsInvalid_returnsOnlyDefaults() {
        expect(githubFieldConfig.default) { githubFieldConfig.forLevel(100) }
    }
}