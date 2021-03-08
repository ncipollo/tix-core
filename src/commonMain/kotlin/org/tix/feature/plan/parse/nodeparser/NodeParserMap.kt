package org.tix.feature.plan.parse.nodeparser

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.tix.feature.plan.parse.parseError

internal class NodeParserMap {
    private val headingParser = HeadingParser()
    private val map = mapOf(
        MarkdownElementTypes.ATX_1 to headingParser,
        MarkdownElementTypes.ATX_2 to headingParser,
        MarkdownElementTypes.ATX_3 to headingParser,
        MarkdownElementTypes.ATX_4 to headingParser,
        MarkdownElementTypes.ATX_5 to headingParser,
        MarkdownElementTypes.ATX_6 to headingParser,
        MarkdownElementTypes.CODE_BLOCK to CodeBlockParser(),
        MarkdownElementTypes.CODE_FENCE to CodeFenceParser(),
        MarkdownElementTypes.EMPH to EmphasisParser(),
        MarkdownElementTypes.LIST_ITEM to ListItemParser(this),
        MarkdownElementTypes.ORDERED_LIST to ListParser(this),
        MarkdownElementTypes.PARAGRAPH to ParagraphParser(this),
        MarkdownElementTypes.STRONG to StrongEmphasisParser(),
        MarkdownElementTypes.UNORDERED_LIST to ListParser(this),
        MarkdownTokenTypes.EOL to LineBreakParser(),
        MarkdownTokenTypes.TEXT to TextParser(),
        MarkdownTokenTypes.WHITE_SPACE to WhitespaceParser(),
    ).mapKeys { it.key.name }

    fun parserForNode(node: ASTNode, markdownText: String) = node.run {
        map[type.name] ?: parseError("Unknown element type: $type", node, markdownText)
    }
}