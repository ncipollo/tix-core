package org.tix.serialize.dynamic

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import org.tix.serialize.TixSerializers
import org.tix.serialize.decodeDynamicElement
import org.tix.serialize.encodeDynamicElement
import kotlin.test.Test
import kotlin.test.assertEquals

class DynamicElementYamlSerializerTest {
    private companion object {
        val MAP = mapOf(
            "bool" to true,
            "double" to 10.5,
            "number" to 1000,
            "string" to "text",
            "list" to listOf(1, "two", mapOf("key" to "value")),
            "map" to mapOf("key" to "value"),
            "null" to null
        )
        val HOLDER = DynamicElementJsonSerializerTest.ElementHolder(DynamicElement(MAP))
    }

    private val yaml = TixSerializers.yaml()

    @Test
    fun deserialize_map() {
        val yamlText = """
            bool: true
            double: 10.5
            number: 1000
            string: text
            list: 
              - 1
              - two
              - key: value
            map: 
              key: value
            "null": null
        """.trimIndent()
        val element = yaml.decodeDynamicElement(yamlText)
        assertEquals(MAP, element.asMap())
    }

    @Test
    fun deserialize_object() {
        val yamlText = """
            element: 
              bool: true
              double: 10.5
              number: 1000
              string: text
              list: 
                - 1
                - two
                - key: value
              map: 
                key: value
              "null": null
        """.trimIndent()
        val element = yaml.decodeFromString<ElementHolder>(yamlText)
        assertEquals(MAP, element.element.value)
    }

    @Test
    fun deserialize_primitive() {
        val element = yaml.decodeFromString<ElementHolder>("element: 10.5")
        assertEquals(ElementHolder(DynamicElement(10.5)), element)
    }

    @Test
    fun serialize_map() {
        val yamlText = yaml.encodeDynamicElement(DynamicElement(MAP))
        val expected = """
            bool: true
            double: 10.5
            number: 1000
            string: text
            list: 
              - 1
              - two
              - key: value
            map: 
              key: value
            null: null
        """.trimIndent()
        assertEquals(expected, yamlText)
    }

    @Test
    fun serialize_object() {
        val yamlText = yaml.encodeToString(HOLDER)
        val expected = """
            element: 
              bool: true
              double: 10.5
              number: 1000
              string: text
              list: 
                - 1
                - two
                - key: value
              map: 
                key: value
              null: null
        """.trimIndent()
        assertEquals(expected, yamlText)
    }

    @Test
    fun serialize_primitive() {
        val yamlText = yaml.encodeToString(ElementHolder(DynamicElement(10.5)))
        assertEquals("element: 10.5", yamlText)
    }

    @Serializable
    data class ElementHolder(@Contextual val element: DynamicElement)
}