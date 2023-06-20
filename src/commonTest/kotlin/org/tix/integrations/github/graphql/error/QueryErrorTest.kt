package org.tix.integrations.github.graphql.error

import kotlin.test.Test
import kotlin.test.expect

class QueryErrorTest {
    @Test
    fun errorStrings() {
        val expected = listOf(
            "- [0:0] message",
            "- [5:10] message"
        )
        expect(expected) {
            QueryError(
                message = "message", locations = listOf(
                    QueryErrorLocation(0, 0),
                    QueryErrorLocation(5, 10),
                )
            ).errorStrings
        }
    }
}