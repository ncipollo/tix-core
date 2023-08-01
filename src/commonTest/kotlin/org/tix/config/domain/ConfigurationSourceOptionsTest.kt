package org.tix.config.domain

import kotlin.test.Test
import kotlin.test.expect

class ConfigurationSourceOptionsTest {
    @Test
    fun forMarkdownSource_noIncludedConfig() {
        val expected = ConfigurationSourceOptions(workspaceDirectory = "/parent")
        expect(expected) {
            ConfigurationSourceOptions.forMarkdownSource("/parent/markdown.md")
        }
    }

    @Test
    fun forMarkdownSource_withIncludedConfig() {
        val expected = ConfigurationSourceOptions(workspaceDirectory = "/parent", savedConfigName = "included")
        expect(expected) {
            ConfigurationSourceOptions.forMarkdownSource("/parent/markdown.md", includeConfigName = "included")
        }
    }
}