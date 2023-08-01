package org.tix.config

import okio.Path.Companion.toPath
import org.junit.Test
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.platform.path.pathByExpandingTilde
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.expect

class ConfigurationPathsTest {
    private val configPaths = ConfigurationPaths()

    @Test
    fun rootConfig() {
        val expectedPaths = listOf("~/.tix/config.yml", "~/.tix/config.json")
            .map { it.pathByExpandingTilde() }
        expect(expectedPaths) { configPaths.rootConfigSearchPaths}
    }

    @Test
    fun workspaceIncludedConfigSearchPaths_whenConfigIsString_returnsNull() {
        val config = RawTixConfiguration(include = DynamicElement("saved"))
        val expectedPaths = listOf("~/.tix/configs/saved.yml", "~/.tix/configs/saved.json")
            .map { it.pathByExpandingTilde() }
        expect(expectedPaths) { configPaths.workspaceIncludedConfigSearchPaths(config) }
    }

    @Test
    fun workspaceIncludedConfigSearchPaths_whenConfigIsStringList_returnsNull() {
        val config = RawTixConfiguration(include = DynamicElement(listOf("saved1", "saved2")))
        val expectedPaths = listOf(
            "~/.tix/configs/saved1.yml",
            "~/.tix/configs/saved1.json",
            "~/.tix/configs/saved2.yml",
            "~/.tix/configs/saved2.json")
            .map { it.pathByExpandingTilde() }
        expect(expectedPaths) { configPaths.workspaceIncludedConfigSearchPaths(config) }
    }

    @Test
    fun workspaceIncludedConfigSearchPaths_whenConfigIsNull_returnsEmptyList() {
        expect(listOf()) { configPaths.workspaceIncludedConfigSearchPaths(null) }
    }

    @Test
    fun workspaceSearchPaths() {
        val workspaceDir = "/path"
        val expectedPaths = listOf("/path/tix.yml", "/path/tix.json")
            .map { it.pathByExpandingTilde() }
        expect(expectedPaths) { configPaths.workspaceSearchPaths(workspaceDir.toPath()) }
    }
}