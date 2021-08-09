package org.tix.integrations.shared.custom

import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.expect

class CustomRequestMethodTest {
    @Test
    fun toHttpMethod() {
        val expected = listOf(HttpMethod.Delete, HttpMethod.Get, HttpMethod.Post, HttpMethod.Put)
        expect(expected) {
            CustomRequestMethod.values().map { it.toHttpMethod() }
        }
    }
}