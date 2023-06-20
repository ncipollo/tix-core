package org.tix.integrations.shared.graphql

import kotlin.test.Test
import kotlin.test.expect

class GraphqlStringExtensionsTest {

    @Test
    fun escapeTripleQuotes() {
        val input = "first quotes \"\"\"\n second quotes \"\"\""
        expect("first quotes \\\"\"\"\n second quotes \\\"\"\"") { input.escapeTripleQuotes() }
    }

    @Test
    fun quote_null() {
        val input: String? = null
        expect(null) { input.quote() }
    }

    @Test
    fun quote_quotesNonNullString() {
        expect("\"text\"") { "text".quote() }
    }
}