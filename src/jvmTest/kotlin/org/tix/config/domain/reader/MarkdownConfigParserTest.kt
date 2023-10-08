package org.tix.config.domain.reader

import org.junit.Test
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.error.TixError
import org.tix.feature.plan.domain.parse.configparser.ConfigLanguage
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.assertFailsWith
import kotlin.test.expect

class MarkdownConfigParserTest {
    private val configParser = MarkdownConfigParser()

    @Test
    fun parse_json_invalidFormat() {
        val json = """
            bad
        """.trimIndent()

        assertFailsWith<TixError> {
            configParser.parse(json, ConfigLanguage.JSON)
        }
    }

    @Test
    fun parse_json_validFormat() {
        val json = """
            {
                "include": "saved"
            }
        """.trimIndent()

        val expected = RawTixConfiguration(include = DynamicElement("saved"))
        expect(expected) { configParser.parse(json, ConfigLanguage.JSON) }
    }

    @Test
    fun parse_noConfig() {
        expect(null) { configParser.parse("code", ConfigLanguage.NO_CONFIG) }
    }

    @Test
    fun parse_yaml_invalidFormat() {
        val json = """
            bad
        """.trimIndent()

        assertFailsWith<TixError> {
            configParser.parse(json, ConfigLanguage.YAML)
        }
    }

    @Test
    fun parse_yaml_validFormat() {
        val json = """
            include: saved
        """.trimIndent()

        val expected = RawTixConfiguration(include = DynamicElement("saved"))
        expect(expected) { configParser.parse(json, ConfigLanguage.YAML) }
    }
}