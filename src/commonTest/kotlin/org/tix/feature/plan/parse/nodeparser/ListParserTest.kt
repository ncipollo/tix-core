package org.tix.feature.plan.parse.nodeparser

import kotlin.test.Test
import kotlin.test.expect

class ListParserTest {
    private val parser = ListParser(NodeParserMap())

    @Test
    fun parse_bullet_mixed() {
        val arguments = """
            - item1
                1. item2
        """.trimIndent().toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments) {
            bulletList {
                bulletListItem {
                    paragraph { text("item1") }
                    linebreak()
                    orderedList(1) {
                        orderedListItem(1, 1) { paragraph { text("item2") } }
                    }
                }
            }
        }
        expect(1) { results.nextIndex }
    }

    @Test
    fun parse_bullet_multipleLines() {
        val arguments = """
            - line1
            line2
            - item2
        """.trimIndent().toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments) {
            bulletList {
                bulletListItem {
                    paragraph {
                        text("line1")
                        linebreak()
                        text("line2")
                    }
                }
                linebreak()
                bulletListItem { paragraph { text("item2") } }
            }
        }
        expect(1) { results.nextIndex }
    }

    @Test
    fun parse_bullet_nestedBullets() {
        val arguments = """
            - item1
                - nested1
                - nested2
            - item2
                - nested3
                - nested4
                    - nested5
                    - nested6
        """.trimIndent().toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments) {
            bulletList {
                bulletListItem {
                    paragraph { text("item1") }
                    linebreak()
                    bulletList(1) {
                        bulletListItem(1) { paragraph { text("nested1") } }
                        linebreak()
                        bulletListItem(1) { paragraph { text("nested2") } }
                    }
                }
                linebreak()
                bulletListItem {
                    paragraph { text("item2") }
                    linebreak()
                    bulletList(1) {
                        bulletListItem(1) { paragraph { text("nested3") } }
                        linebreak()
                        bulletListItem(1) {
                            paragraph { text("nested4") }
                            linebreak()
                            bulletList(2) {
                                bulletListItem(2) { paragraph { text("nested5") } }
                                linebreak()
                                bulletListItem(2) { paragraph { text("nested6") } }
                            }
                        }
                    }
                }
            }
        }
        expect(1) { results.nextIndex }
    }

    @Test
    fun parse_bullet_singleLevel() {
        val arguments = """
            - item1
            - item2
        """.trimIndent().toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments) {
            bulletList {
                bulletListItem { paragraph { text("item1") } }
                linebreak()
                bulletListItem { paragraph { text("item2") } }
            }
        }
        expect(1) { results.nextIndex }
    }

    @Test
    fun parse_ordered_nestedBullets() {
        val arguments = """
            1. item1
                10) nested1
                11) nested2
            2. item2
                1. nested3
                2. nested4
                    1. nested5
                    2. nested6
        """.trimIndent().toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments) {
            orderedList {
                orderedListItem(0, 1) {
                    paragraph { text("item1") }
                    linebreak()
                    orderedList(1) {
                        orderedListItem(1, 10) { paragraph { text("nested1") } }
                        linebreak()
                        orderedListItem(1, 11) { paragraph { text("nested2") } }
                    }
                }
                linebreak()
                orderedListItem(0, 2) {
                    paragraph { text("item2") }
                    linebreak()
                    orderedList(1) {
                        orderedListItem(1, 1) { paragraph { text("nested3") } }
                        linebreak()
                        orderedListItem(1, 2) {
                            paragraph { text("nested4") }
                            linebreak()
                            orderedList(2) {
                                orderedListItem(2, 1) { paragraph { text("nested5") } }
                                linebreak()
                                orderedListItem(2, 2) { paragraph { text("nested6") } }
                            }
                        }
                    }
                }
            }
        }
        expect(1) { results.nextIndex }
    }

    @Test
    fun parse_ordered_singleLevel() {
        val arguments = """
            1. item1
            2. item2
        """.trimIndent().toParserArguments()
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expectBody(arguments) {
            orderedList {
                orderedListItem(0, 1) { paragraph { text("item1") } }
                linebreak()
                orderedListItem(0, 2) { paragraph { text("item2") } }
            }
        }
        expect(1) { results.nextIndex }
    }
}