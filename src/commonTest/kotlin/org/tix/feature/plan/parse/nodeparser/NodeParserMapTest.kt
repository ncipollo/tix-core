package org.tix.feature.plan.parse.nodeparser

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.LeafASTNode
import kotlin.test.Test
import kotlin.test.expect

class NodeParserMapTest {
    private val parserMap = NodeParserMap()

    @Test
    fun parserForElementType_bulletList() {
        val node = LeafASTNode(MarkdownElementTypes.UNORDERED_LIST, 0, 1)
        expect(ListParser::class) { parserMap.parserForNode(node, "")::class }
    }

    @Test
    fun parserForElementType_codeBlock() {
        val node = LeafASTNode(MarkdownElementTypes.CODE_BLOCK, 0, 1)
        expect(CodeBlockParser::class) { parserMap.parserForNode(node, "")::class }
    }

    @Test
    fun parserForElementType_codeFence() {
        val node = LeafASTNode(MarkdownElementTypes.CODE_FENCE, 0, 1)
        expect(CodeFenceParser::class) { parserMap.parserForNode(node, "")::class }
    }

    @Test
    fun parserForElementType_emphasis() {
        val node = LeafASTNode(MarkdownElementTypes.EMPH, 0, 1)
        expect(EmphasisParser::class) { parserMap.parserForNode(node, "")::class }
    }

    @Test
    fun parserForElementType_listItem() {
        val node = LeafASTNode(MarkdownElementTypes.LIST_ITEM, 0, 1)
        expect(ListItemParser::class) { parserMap.parserForNode(node, "")::class }
    }

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
            expect(HeadingParser::class) { parserMap.parserForNode(node, "")::class }
        }
    }

    @Test
    fun parserForElementType_lineBreakType() {
        val node = LeafASTNode(MarkdownTokenTypes.EOL, 0, 1)
        expect(LineBreakParser::class) { parserMap.parserForNode(node, "")::class }
    }

    @Test
    fun parserForElementType_orderedList() {
        val node = LeafASTNode(MarkdownElementTypes.ORDERED_LIST, 0, 1)
        expect(ListParser::class) { parserMap.parserForNode(node, "")::class }
    }

    @Test
    fun parserForElementType_paragraphType() {
        val node = LeafASTNode(MarkdownElementTypes.PARAGRAPH, 0, 3)
        expect(ParagraphParser::class) { parserMap.parserForNode(node, "")::class }
    }

    @Test
    fun parserForElementType_strongEmphasis() {
        val node = LeafASTNode(MarkdownElementTypes.STRONG, 0, 1)
        expect(StrongEmphasisParser::class) { parserMap.parserForNode(node, "")::class }
    }

    @Test
    fun parserForElementType_textType() {
        val node = LeafASTNode(MarkdownTokenTypes.TEXT, 0, 3)
        expect(TextParser::class) { parserMap.parserForNode(node, "")::class }
    }

    @Test
    fun parserForElementType_whitespaceType() {
        val node = LeafASTNode(MarkdownTokenTypes.WHITE_SPACE, 0, 1)
        expect(WhitespaceParser::class) { parserMap.parserForNode(node, "")::class }
    }
}