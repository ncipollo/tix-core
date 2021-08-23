package org.tix.serialize.dynamic

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.tix.serialize.TixSerializers
import org.tix.serialize.decodeDynamicElement
import org.tix.serialize.encodeDynamicElement
import kotlin.test.Test
import kotlin.test.assertEquals

class DynamicElementJsonSerializerTest {
    private companion object {
        val JSON_TEXT = """
            {
                "element": {
                    "bool": true,
                    "double": 10.5,
                    "long": 1000,
                    "string": "text",
                    "list": [
                        1,
                        "two",
                        {
                            "key": "value"
                        }
                    ],
                    "map": {
                        "key": "value"
                    },
                    "null": null
                }
            }
        """.trimIndent()
        val MAP = mapOf(
            "bool" to true,
            "double" to 10.5,
            "long" to 1000L,
            "string" to "text",
            "list" to listOf(1L, "two", mapOf("key" to "value")),
            "map" to mapOf("key" to "value"),
            "null" to null
        )
        val HOLDER = ElementHolder(DynamicElement(MAP))
    }

    private val json = TixSerializers.json()

    @Test
    fun deserialize_map() {
        val jsonText = """
        {    
            "bool": true,
            "double": 10.5,
            "long": 1000,
            "string": "text",
            "list": [
                1,
                "two",
                {
                    "key": "value"
                }
            ],
            "map": {
                "key": "value"
            },
            "null": null
        }    
        """.trimIndent()
        val element = json.decodeDynamicElement(jsonText)
        assertEquals(MAP, element.asMap())
    }

    @Test
    fun deserialize_object() {
        val holder = json.decodeFromString<ElementHolder>(JSON_TEXT)
        assertEquals(MAP, holder.element.value)
    }

    @Test
    fun deserialize_primitive() {
        val jsonText = """
            {
                "element": 10.5
            }
        """.trimIndent()

        val holder = json.decodeFromString<ElementHolder>(jsonText)
        assertEquals(10.5, holder.element.value)
    }

    @Test
    fun serialize_map() {
        val expectedText = """
        {
            "bool": true,
            "double": 10.5,
            "long": 1000,
            "string": "text",
            "list": [
                1,
                "two",
                {
                    "key": "value"
                }
            ],
            "map": {
                "key": "value"
            },
            "null": null
        }
        """.trimIndent()
        val jsonText = json.encodeDynamicElement(DynamicElement(MAP))
        assertEquals(expectedText, jsonText)
    }

    @Test
    fun serialize_object() {
        val jsonText = json.encodeToString(HOLDER)
        assertEquals(JSON_TEXT, jsonText)
    }

    @Test
    fun serialize_primitive() {
        val jsonText = json.encodeToString(ElementHolder(DynamicElement(10.5)))
        val expectedJson = """
            {
                "element": 10.5
            }
        """.trimIndent()
        assertEquals(expectedJson, jsonText)
    }

    @Serializable
    data class ElementHolder(@Contextual val element: DynamicElement)
}