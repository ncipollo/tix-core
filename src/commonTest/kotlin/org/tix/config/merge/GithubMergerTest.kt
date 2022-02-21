package org.tix.config.merge

import org.tix.config.data.auth.AuthSource
import org.tix.config.data.raw.RawAuthConfiguration
import org.tix.config.data.raw.RawGithubConfiguration
import org.tix.config.data.raw.RawGithubFieldConfiguration
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.Test
import kotlin.test.expect

class GithubMergerTest {
    private val base = RawGithubConfiguration(
        auth = RawAuthConfiguration(AuthSource.ENV, "base-auth.json"),
        owner = "baseOwner",
        repo = "baseRepo",
        noProjects = false,
        fields = RawGithubFieldConfiguration(
            default = mapOf("base" to DynamicElement("default")),
            project = mapOf("base" to DynamicElement("project")),
            issue = mapOf("base" to DynamicElement("issue"))
        )
    )

    @Test
    fun merge_whenOverlayIsBlank_returnsBaseConfiguration() {
        val overlay = RawGithubConfiguration()
        expect(base) { base.merge(overlay) }
    }

    @Test
    fun merge_whenOverlayIsNull_returnsBaseConfiguration() {
        val overlay = RawGithubConfiguration()
        expect(base) { base.merge(overlay) }
    }

    @Test
    fun merge_whenOverlayFullyPopulated_returnsOverlayConfiguration() {
        val overlay = RawGithubConfiguration(
            auth = RawAuthConfiguration(AuthSource.LOCAL_FILE, "overlay-auth.json"),
            owner = "overlayOwner",
            repo = "overlayRepo",
            noProjects = true,
            fields = RawGithubFieldConfiguration(
                default = mapOf("overlay" to DynamicElement("default")),
                project = mapOf("overlay" to DynamicElement("project")),
                issue = mapOf("overlay" to DynamicElement("issue"))
            )
        )
        val expected = RawGithubConfiguration(
            auth = RawAuthConfiguration(AuthSource.LOCAL_FILE, "overlay-auth.json"),
            owner = "overlayOwner",
            repo = "overlayRepo",
            noProjects = true,
            fields = RawGithubFieldConfiguration(
                default = base.fields.default + mapOf("overlay" to DynamicElement("default")),
                project = base.fields.project + mapOf("overlay" to DynamicElement("project")),
                issue = base.fields.issue + mapOf("overlay" to DynamicElement("issue"))
            )
        )
        expect(expected) { base.merge(overlay) }
    }
}