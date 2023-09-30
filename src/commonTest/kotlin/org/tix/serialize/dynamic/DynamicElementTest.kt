package org.tix.serialize.dynamic

import kotlin.test.Test
import kotlin.test.expect

class DynamicElementTest {
    @Test
    fun asBoolean_valueIsBoolean() = expect(true) { DynamicElement(true).asBoolean() }

    @Test
    fun asBoolean_valueIsNumber_numberIsGreatThanZero() = expect(true) { DynamicElement(1).asBoolean() }

    @Test
    fun asBoolean_valueIsNumber_numberIsZero() = expect(false) { DynamicElement(0).asBoolean() }

    @Test
    fun asDouble() = expect(10.5) { DynamicElement(10.5).asDouble() }

    @Test
    fun asInt() = expect(10) { DynamicElement(10).asInt() }

    @Test
    fun asInt_notANumber() = expect(0) { DynamicElement("hi").asInt() }

    @Test
    fun asInt_isNull() = expect(0) { DynamicElement().asInt() }

    @Test
    fun asLong() = expect(10L) { DynamicElement(10).asLong() }

    @Test
    fun asString_valueIsNull() = expect("") { DynamicElement().asString() }

    @Test
    fun asString_valueIsNumber() = expect("10") { DynamicElement(10).asString() }

    @Test
    fun asString_valueIsString() = expect("text") { DynamicElement("text").asString() }

    @Test
    fun asList_valueIsList() = expect(listOf(1, 2)) { DynamicElement(listOf(1, 2)).asList() }

    @Test
    fun asList_valueIsNotAList() = expect(listOf(1)) { DynamicElement(1).asList() }

    @Test
    fun asList_valueIsNull() = expect(emptyList<Nothing>()) { DynamicElement().asList() }

    @Test
    fun asMap_valueIsAMap() = expect(mapOf("key" to "value")) { DynamicElement(mapOf("key" to "value")).asMap() }

    @Test
    fun asMap_valueIsNotAMap_defaultKeyIsSet() =
        expect(mapOf("key" to "value")) { DynamicElement("value").asMap(defaultKey = "key") }

    @Test
    fun asMap_valueIsNotAMap_defaultKeyIsNotSet() = expect(emptyMap()) { DynamicElement("key").asMap() }

    @Test
    fun asMap_valueIsNull() = expect(emptyMap()) { DynamicElement(null).asMap() }

    @Test
    fun asStringMap_valueIsAMap() =
        expect(mapOf("key" to "value")) { DynamicElement(mapOf("key" to "value")).asStringMap() }

    @Test
    fun asStringMap_valueIsNotAMap_defaultKeyIsSet() =
        expect(mapOf("key" to "value")) { DynamicElement("value").asStringMap(defaultKey = "key") }

    @Test
    fun asStringMap_valueIsNotAMap_defaultKeyIsNotSet() = expect(emptyMap()) { DynamicElement("key").asStringMap() }

    @Test
    fun aStringsMap_valueIsNull() = expect(emptyMap()) { DynamicElement(null).asStringMap() }

    @Test
    fun isNotEmpty_valueIsNull() = expect(false) { DynamicElement(null).isNotEmpty() }

    @Test
    fun isNotEmpty_valueIsNotNull() = expect(true) { DynamicElement(1).isNotEmpty() }
}