package org.tix.feature.plan.parse.nodeparser

import org.intellij.markdown.MarkdownElementTypes
import kotlin.test.Test
import kotlin.test.expect

class NodeParserMapTest {
    private val parserMap = NodeParserMap()

    @Test
    fun parserForElementType_headerTypes() {
        val headerTypes = listOf(
            MarkdownElementTypes.ATX_1,
            MarkdownElementTypes.ATX_2,
            MarkdownElementTypes.ATX_3,
            MarkdownElementTypes.ATX_4,
            MarkdownElementTypes.ATX_5,
            MarkdownElementTypes.ATX_6,
        )
        headerTypes.forEach { type ->
            expect(HeadingParser::class) { parserMap.parserForElementType(type)::class }
        }
    }
}