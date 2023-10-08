package org.tix.config.domain.reader

import org.junit.Test
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.error.TixError
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.assertFailsWith
import kotlin.test.expect

class MarkdownConfigurationReaderTest {
    private val configReader = MarkdownConfigurationReader()

    @Test
    fun configFromMarkdown_hasConfig_beforeSection() {
        val markdown = """
            ```tix_config
            include: markdown_config
            ```
            # First Section
        """.trimIndent()

        val expectedConfig = RawTixConfiguration(include = DynamicElement("markdown_config"))
        expect(expectedConfig) { configReader.configFromMarkdown(markdown) }
    }

    @Test
    fun configFromMarkdown_hasConfig_beforeSection_deepSection() {
        val markdown = """
            ```tix_config
            include: markdown_config
            ```
            ### First Section
        """.trimIndent()

        val expectedConfig = RawTixConfiguration(include = DynamicElement("markdown_config"))
        expect(expectedConfig) { configReader.configFromMarkdown(markdown) }
    }

    @Test
    fun configFromMarkdown_hasConfig_beforeSection_withSurroundingElements() {
        val markdown = """
            text before
            ```tix
            include: markdown_config
            ```
            text after
            # First Section
            Text in section
        """.trimIndent()

        val expectedConfig = RawTixConfiguration(include = DynamicElement("markdown_config"))
        expect(expectedConfig) { configReader.configFromMarkdown(markdown) }
    }

    @Test
    fun configFromMarkdown_hasConfig_multipleConfigs() {
        val markdown = """
            ```tix_config
            include: first_config
            ```
            ```tix_config
            include: second_config
            ```
            # First Section
        """.trimIndent()

        val expectedConfig = RawTixConfiguration(include = DynamicElement("first_config"))
        expect(expectedConfig) { configReader.configFromMarkdown(markdown) }
    }

    @Test
    fun configFromMarkdown_hasConfig_multipleConfigs_afterSection() {
        val markdown = """
            ```tix_config
            include: first_config
            ```
            # First Section
            
            ```tix_config
            include: second_config
            ```
        """.trimIndent()

        val expectedConfig = RawTixConfiguration(include = DynamicElement("first_config"))
        expect(expectedConfig) { configReader.configFromMarkdown(markdown) }
    }

    @Test
    fun configFromMarkdown_hasConfig_noSection() {
        val markdown = """
            ```tix
            include: markdown_config
            ```
        """.trimIndent()

        val expectedConfig = RawTixConfiguration(include = DynamicElement("markdown_config"))
        expect(expectedConfig) { configReader.configFromMarkdown(markdown) }
    }

    @Test
    fun configFromMarkdown_noConfig_afterSection() {
        val markdown = """
            # First Section
            ```tix_config
            include: markdown_config
            ```
        """.trimIndent()

        expect(null) { configReader.configFromMarkdown(markdown) }
    }

    @Test
    fun configFromMarkdown_noConfig_fenceIsNotTixConfig() {
        val markdown = """
            # First Section
            ```yml
            include: markdown_config
            ```
        """.trimIndent()

        expect(null) { configReader.configFromMarkdown(markdown) }
    }

    @Test
    fun configFromMarkdown_noConfig_noPreamble() {
        val markdown = """
            # First Section
        """.trimIndent()

        expect(null) { configReader.configFromMarkdown(markdown) }
    }

    @Test
    fun configFromMarkdown_noConfig_noText() {
        val markdown = ""
        expect(null) { configReader.configFromMarkdown(markdown) }
    }

    @Test
    fun configFromMarkdown_noConfig_nullText() {
        expect(null) { configReader.configFromMarkdown(null) }
    }

    @Test
    fun configFromMarkdown_throws_malformedConfig() {
        val markdown = """
            ```tix_json
            include: markdown_config
            ```
            # First Section
        """.trimIndent()

        assertFailsWith<TixError> { configReader.configFromMarkdown(markdown) }
    }
}