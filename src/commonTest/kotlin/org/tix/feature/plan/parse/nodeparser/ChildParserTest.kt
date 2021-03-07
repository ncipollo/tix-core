package org.tix.feature.plan.parse.nodeparser

import org.intellij.markdown.MarkdownElementTypes
import org.tix.model.ticket.body.EmphasisSegment
import org.tix.model.ticket.body.StrongEmphasisSegment
import org.tix.model.ticket.body.TextSegment
import org.tix.model.ticket.body.WhitespaceSegment
import kotlin.test.Test
import kotlin.test.assertEquals

class ChildParserTest {
    private val arguments = "text *emph* **strong**".toParserArguments()
    private val parserMap = NodeParserMap()

    @Test
    fun parseChildren_parsesAllChildren() {
        arguments.state.startTicket()

        val results = parseChildren(arguments, parserMap)

        val expectedSegments = listOf(
            TextSegment("text"),
            WhitespaceSegment(1),
            EmphasisSegment("emph"),
            WhitespaceSegment(1),
            StrongEmphasisSegment("strong"),
        )
        assertEquals(expectedSegments, results)
    }

    @Test
    fun parseFilteredChildren() {
        arguments.state.startTicket()

        val results = parseFilteredChildren(arguments, parserMap) {
            it.type.name != MarkdownElementTypes.EMPH.name
        }

        val expectedSegments = listOf(
            TextSegment("text"),
            WhitespaceSegment(1),
            WhitespaceSegment(1),
            StrongEmphasisSegment("strong"),
        )
        assertEquals(expectedSegments, results)
    }
}