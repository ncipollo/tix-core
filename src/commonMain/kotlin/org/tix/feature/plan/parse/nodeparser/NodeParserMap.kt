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
        MarkdownTokenTypes.EOL to LineBreakParser(),
    ).mapKeys { it.key.name }

    fun parserForNode(node: ASTNode, markdownText: String) = node.run {
        map[type.name] ?: parseError("Unknown element type: $type", node, markdownText)
    }
}