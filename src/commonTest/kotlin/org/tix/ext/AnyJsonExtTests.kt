package org.tix.ext

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.expect

class AnyJsonExtTests {
    @Test
    fun toJsonPrimitive_boolean() {
        expect(JsonPrimitive(true)) {
            true.toJsonPrimitive()
        }
    }

    @Test
    fun toJsonPrimitive_null() {
        expect(JsonNull) {
            null.toJsonPrimitive()
        }
    }

    @Test
    fun toJsonPrimitive_number_double() {
        expect(JsonPrimitive(42.0)) {
            42.0.toJsonPrimitive()
        }
    }

    @Test
    fun toJsonPrimitive_number_float() {
        expect(JsonPrimitive(42.0f)) {
            42.0f.toJsonPrimitive()
        }
    }

    @Test
    fun toJsonPrimitive_number_int() {
        expect(JsonPrimitive(42)) {
            42.toJsonPrimitive()
        }
    }

    @Test
    fun toJsonPrimitive_number_long() {
        expect(JsonPrimitive(42L)) {
            42L.toJsonPrimitive()
        }
    }

    @Test
    fun toJsonPrimitive_string() {
        expect(JsonPrimitive("text")) {
            "text".toJsonPrimitive()
        }
    }

    @Test
    fun toJsonPrimitive_unknownType() {
        expect(JsonNull) {
            mapOf("foo" to "bar").toJsonPrimitive()
        }
    }
}