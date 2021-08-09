package org.tix.config

import okio.Path.Companion.toPath
import org.junit.Test
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.platform.path.pathByExpandingTilde
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.expect

class ConfigurationPathsTest {
    @Test
    fun rootConfig() {
        val expectedPaths = listOf("~/.tix/config.yml", "~/.tix/config.json")
            .map { it.pathByExpandingTilde() }
        expect(expectedPaths) { ConfigurationPaths.RootConfig.searchPaths }
    }

    @Test
    fun savedConfigSearchPaths_whenConfigIsString_returnsNull() {
        val config = RawTixConfiguration(include = DynamicElement("saved"))
        val expectedPaths = listOf("~/.tix/configs/saved.yml", "~/.tix/configs/saved.json")
            .map { it.pathByExpandingTilde() }
        expect(expectedPaths) { ConfigurationPaths.savedConfigSearchPaths(config) }
    }

    @Test
    fun savedConfigSearchPaths_whenConfigIsStringList_returnsNull() {
        val config = RawTixConfiguration(include = DynamicElement(listOf("saved1", "saved2")))
        val expectedPaths = listOf(
            "~/.tix/configs/saved1.yml",
            "~/.tix/configs/saved1.json",
            "~/.tix/configs/saved2.yml",
            "~/.tix/configs/saved2.json")
            .map { it.pathByExpandingTilde() }
        expect(expectedPaths) { ConfigurationPaths.savedConfigSearchPaths(config) }
    }

    @Test
    fun savedConfigSearchPaths_whenConfigIsNull_returnsNull() {
        expect(null) { ConfigurationPaths.savedConfigSearchPaths(null) }
    }

    @Test
    fun workspaceSearchPaths() {
        val markdownPath = "/path/tix.md"
        val expectedPaths = listOf("/path/tix.yml", "/path/tix.json")
            .map { it.pathByExpandingTilde() }
        expect(expectedPaths) { ConfigurationPaths.workspaceSearchPaths(markdownPath.toPath()) }
    }
}