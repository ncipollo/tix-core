package org.tix.integrations.github.graphql.error

import kotlin.test.Test
import kotlin.test.expect

class QueryErrorLocationTest {
    @Test
    fun toString_format() {
        expect("[10:20]") {
            QueryErrorLocation(10, 20).toString()
        }
    }
}