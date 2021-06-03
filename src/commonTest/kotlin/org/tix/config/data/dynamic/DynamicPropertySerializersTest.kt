package org.tix.config.data.dynamic

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.tix.config.data.GithubFieldConfiguration
import org.tix.config.serialize.ConfigurationSerializers
import kotlin.test.Test
import kotlin.test.expect

class DynamicPropertySerializersTest {
    private val config = GithubFieldConfiguration(
        default = mapOf(
            "boolean" to DynamicProperty(boolean = true),
            "double" to DynamicProperty(number = 3.14),
            "long" to DynamicProperty(number = 1000L),
            "string" to DynamicProperty(string = "a"),
            "stringList" to DynamicProperty(stringList = listOf("a", "1", "b")),
        )
    )
    private val json = ConfigurationSerializers.json()
    private val yaml = ConfigurationSerializers.yaml()

    @Test
    fun deserialize_json() {
        val jsonText = """
            {
                "default": {
                    "boolean": true,
                    "double": 3.14,
                    "long": 1000,
                    "string": "a",
                    "stringList": ["a", 1, "b"]
                }
            }
        """.trimIndent()
        expect(config) {
            json.decodeFromString(jsonText)
        }
    }

    @Test
    fun deserialize_yaml() {
        val yamlText = """
            default:
                boolean: true
                double: 3.14
                long: 1000
                string: a
                stringList: [a, 1, b]
        """.trimIndent()
        // Doubles are getting rounded due to a parser bug. See https://github.com/Him188/yamlkt/issues/28.
        val expectedConfig = GithubFieldConfiguration(
            default = mapOf(
                "boolean" to DynamicProperty(boolean = true),
                "double" to DynamicProperty(number = 3),
                "long" to DynamicProperty(number = 1000),
                "string" to DynamicProperty(string = "a"),
                "stringList" to DynamicProperty(stringList = listOf("a", "1", "b")),
            )
        )
        expect(expectedConfig) {
            yaml.decodeFromString(yamlText)
        }
    }

    @Test
    fun serialize_json() {
        val jsonText = """
            {
                "default": {
                    "boolean": true,
                    "double": 3.14,
                    "long": 1000,
                    "string": "a",
                    "stringList": [
                        "a",
                        "1",
                        "b"
                    ]
                }
            }
        """.trimIndent()
        expect(jsonText) {
            json.encodeToString(config)
        }
    }

    @Test
    fun serialize_yaml() {
        val yamlText = """
            default: 
              boolean: true
              double: 3.14
              long: 1000
              string: a
              stringList: 
                - a
                - 1
                - b
        """.trimIndent()
        expect(yamlText) {
            yaml.encodeToString(config)
        }
    }
}