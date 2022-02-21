package org.tix.ext

import kotlin.test.Test
import kotlin.test.expect

class MapExtTest {
    private val map = mapOf(
        "number" to 42,
        "list" to listOf(1, 2, 3),
        "multipleTypes" to listOf(1, "2", 3),
    )

    @Test
    fun transform_valueIsExpectedType() {
        expect("number=42") { map.transform("number", 0) { "number=$it" } }
    }

    @Test
    fun transform_valueIsNotExpectedType() {
        expect("number=default") { map.transform("number", "default") { "number=$it" } }
    }

    @Test
    fun transformFilteredList() {
        val expected = listOf("number=2")
        expect(expected) {
            map.transformFilteredList<String, String>("multipleTypes") { "number=$it" }
        }
    }

    @Test
    fun transformList_listHasMultipleTypes() {
        val expected = listOf(
            "number=1",
            "number=0",
            "number=3"
        )
        expect(expected) { map.transformList("multipleTypes", 0) { "number=$it" } }
    }

    @Test
    fun transformList_valueIsExpectedType() {
        val expected = listOf(
            "number=1",
            "number=2",
            "number=3"
        )
        expect(expected) { map.transformList("list", 0) { "number=$it" } }
    }

    @Test
    fun transformList_valueIsNotExpectedType() {
        val expected = listOf(
            "number=default",
            "number=default",
            "number=default"
        )
        expect(expected) { map.transformList("list", "default") { "number=$it" } }
    }
}