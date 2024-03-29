package org.tix.feature.plan.domain.parse.fieldparser

import org.tix.feature.plan.domain.parse.configparser.ConfigLanguage
import org.tix.serialize.dynamic.DynamicElement
import org.tix.serialize.dynamic.emptyDynamic
import kotlin.test.Test
import kotlin.test.expect

class FieldParserTest {
    private companion object {
        val MAP = mapOf("key1" to "value1", "key2" to "value2")
        val DYNAMIC = DynamicElement(MAP)
    }

    @Test
    fun parse_json() {
        val code = """
        {
            "key1": "value1",
            "key2": "value2"
        }
        """.trimIndent()
        expect(DYNAMIC) { FieldParser.parse(code, language = ConfigLanguage.JSON) }
    }

    @Test
    fun parse_noFields() {
        val code = """
        {
            "key1": "value1",
            "key2": "value2"
        }
        """.trimIndent()
        expect(emptyDynamic()) { FieldParser.parse(code, ConfigLanguage.NO_CONFIG) }
    }

    @Test
    fun parse_yaml() {
        val code = """
        key1: value1
        key2: value2
        """.trimIndent()
        expect(DYNAMIC) { FieldParser.parse(code, ConfigLanguage.YAML) }
    }
}