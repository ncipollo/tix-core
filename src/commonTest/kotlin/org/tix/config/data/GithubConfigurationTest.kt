package org.tix.config.data

import org.tix.fixture.config.githubConfig
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.Test
import kotlin.test.expect

class GithubConfigurationTest {
    @Test
    fun fieldsForLevel_whenProjectsAreDisabled_returnsFieldsForProvidedLevel() {
        val config = githubConfig.copy(noProjects = true)
        val expected = mapOf(
            "common" to DynamicElement("issue"),
            "unique0" to DynamicElement("default"),
            "unique2" to DynamicElement("issue"),
        )
        expect(expected) { config.fieldsForLevel(0) }
    }

    @Test
    fun fieldsForLevel_whenProjectsAreEnabled_returnsFieldsForProvidedLevel() {
        val expected = mapOf(
            "common" to DynamicElement("project"),
            "unique0" to DynamicElement("default"),
            "unique1" to DynamicElement("project"),
        )
        expect(expected) { githubConfig.fieldsForLevel(0) }
    }
}