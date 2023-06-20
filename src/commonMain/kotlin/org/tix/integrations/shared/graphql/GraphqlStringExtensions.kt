package org.tix.integrations.shared.graphql

fun String?.quote() = this?.let { "\"$it\"" }

fun String.escapeTripleQuotes() = replace("\"\"\"", "\\\"\"\"")