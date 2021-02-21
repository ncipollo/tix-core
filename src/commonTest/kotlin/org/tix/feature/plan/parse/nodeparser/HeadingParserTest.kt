package org.tix.feature.plan.parse.nodeparser

import kotlin.test.Test

class HeadingParserTest {
    private val arguments = """
        # Section 1
        Body 1
        # Section 2
        Body 2
    """.trimIndent().toParserArguments()
    private val parser = HeadingParser()

    @Test
    fun parse() {
        parser.parse(arguments)
    }
}