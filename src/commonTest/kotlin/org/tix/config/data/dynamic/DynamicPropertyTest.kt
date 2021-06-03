package org.tix.config.data.dynamic

import kotlin.test.Test
import kotlin.test.assertEquals

class DynamicPropertyTest {
    @Test
    fun asBoolean() {
        val actual = listOf(
            DynamicProperty(boolean = true),
            DynamicProperty(number = 1),
            DynamicProperty(),
        ).map { it.asBoolean() }
        val expected = listOf(true, true, false)
        assertEquals(expected, actual)
    }

    @Test
    fun asDouble() {
        val actual = listOf(
            DynamicProperty(number = 1),
            DynamicProperty(),
        ).map { it.asDouble() }
        val expected = listOf(1.0, 0)
        assertEquals(expected, actual)
    }

    @Test
    fun asLong() {
        val actual = listOf(
            DynamicProperty(number = 1),
            DynamicProperty(),
        ).map { it.asLong() }
        val expected = listOf(1L, 0)
        assertEquals(expected, actual)
    }

    @Test
    fun asString() {
        val actual = listOf(
            DynamicProperty(string = "string"),
            DynamicProperty(stringList = listOf("string1", "string2")),
            DynamicProperty(boolean = true),
            DynamicProperty(number = 1),
            DynamicProperty(),
        ).map { it.asString() }
        val expected = listOf("string", "string1", "true", "1", "")
        assertEquals(expected, actual)
    }

    @Test
    fun asStringList() {
        val actual = listOf(
            DynamicProperty(stringList = listOf("string1", "string2")),
            DynamicProperty(string = "string"),
            DynamicProperty(),
        ).map { it.asStringList() }
        val expected = listOf(
            listOf("string1", "string2"),
            listOf("string"),
            emptyList()
        )
        assertEquals(expected, actual)
    }
}