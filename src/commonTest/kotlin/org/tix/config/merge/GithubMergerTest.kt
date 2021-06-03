package org.tix.config.merge

import org.tix.config.data.GithubConfiguration
import org.tix.config.data.GithubFieldConfiguration
import org.tix.config.data.dynamic.DynamicProperty
import kotlin.test.Test
import kotlin.test.expect

class GithubMergerTest {
    private val base = GithubConfiguration(
        owner = "baseOwner",
        repo = "baseRepo",
        noProjects = false,
        fields = GithubFieldConfiguration(
            default = mapOf("base" to DynamicProperty(string = "default")),
            project = mapOf("base" to DynamicProperty(string = "project")),
            issue = mapOf("base" to DynamicProperty(string = "issue"))
        )
    )

    @Test
    fun merge_whenOverlayIsBlank_returnsBaseConfiguration() {
        val overlay = GithubConfiguration()
        expect(base) { base.merge(overlay) }
    }

    @Test
    fun merge_whenOverlayFullyPopulated_returnsBaseConfiguration() {
        val overlay = GithubConfiguration(
            owner = "overlayOwner",
            repo = "overlayRepo",
            noProjects = true,
            fields = GithubFieldConfiguration(
                default = mapOf("overlay" to DynamicProperty(string = "default")),
                project = mapOf("overlay" to DynamicProperty(string = "project")),
                issue = mapOf("overlay" to DynamicProperty(string = "issue"))
            )
        )
        val expected = GithubConfiguration(
            owner = "overlayOwner",
            repo = "overlayRepo",
            noProjects = true,
            fields = GithubFieldConfiguration(
                default = base.fields.default + mapOf("overlay" to DynamicProperty(string = "default")),
                project = base.fields.project + mapOf("overlay" to DynamicProperty(string = "project")),
                issue = base.fields.issue + mapOf("overlay" to DynamicProperty(string = "issue"))
            )
        )
        expect(expected) { base.merge(overlay) }
    }
}