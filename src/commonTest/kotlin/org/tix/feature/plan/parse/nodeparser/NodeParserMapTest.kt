package org.tix.feature.plan.parse.nodeparser

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.LeafASTNode
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
            val node = LeafASTNode(type, 0, 1)
            expect(HeadingParser::class) { parserMap.parserForNode(node, "markdown text")::class }
        }
    }

    @Test
    fun parserForElementType_lineBreakType() {
        val node = LeafASTNode(MarkdownTokenTypes.EOL, 0, 1)
        expect(LineBreakParser::class) { parserMap.parserForNode(node, "markdown text")::class }
    }
}