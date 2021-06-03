package org.tix.config.merge

import org.tix.config.data.JiraConfiguration
import org.tix.config.data.JiraFieldConfiguration
import org.tix.config.data.dynamic.DynamicProperty
import kotlin.test.Test
import kotlin.test.expect

class JiraMergerTest {
    private val base = JiraConfiguration(
        noEpics = false,
        fields = JiraFieldConfiguration(
            default = mapOf("base" to DynamicProperty(string = "default")),
            epic = mapOf("base" to DynamicProperty(string = "epic")),
            issue = mapOf("base" to DynamicProperty(string = "issue")),
            task = mapOf("base" to DynamicProperty(string = "task"))
        ),
        url = "baseUrl"
    )

    @Test
    fun merge_whenOverlayIsBlank_returnsBaseConfiguration() {
        val overlay = JiraConfiguration()
        expect(base) { base.merge(overlay) }
    }

    @Test
    fun merge_whenOverlayFullyPopulated_returnsBaseConfiguration() {
        val overlay = JiraConfiguration(
            noEpics = true,
            fields = JiraFieldConfiguration(
                default = mapOf("overlay" to DynamicProperty(string = "default")),
                epic = mapOf("overlay" to DynamicProperty(string = "epic")),
                issue = mapOf("overlay" to DynamicProperty(string = "issue")),
                task = mapOf("overlay" to DynamicProperty(string = "task"))
            ),
            url = "overlayUrl"
        )
        val expected = JiraConfiguration(
            noEpics = true,
            fields = JiraFieldConfiguration(
                default = base.fields.default + mapOf("overlay" to DynamicProperty(string = "default")),
                epic = base.fields.epic + mapOf("overlay" to DynamicProperty(string = "epic")),
                issue = base.fields.issue + mapOf("overlay" to DynamicProperty(string = "issue")),
                task = base.fields.task + mapOf("overlay" to DynamicProperty(string = "task"))
            ),
            url = "overlayUrl"
        )
        expect(expected) { base.merge(overlay) }
    }
}