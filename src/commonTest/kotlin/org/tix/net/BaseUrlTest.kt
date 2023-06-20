package org.tix.net

import kotlin.test.Test
import kotlin.test.expect

class BaseUrlTest {
    private val baseUrl = BaseUrl("https://api.example.com")

    @Test
    fun toString_test() {
        expect("https://api.example.com") { baseUrl.toString() }
    }

    @Test
    fun withPath() {
        expect("https://api.example.com/path") { baseUrl.withPath("path") }
    }
}