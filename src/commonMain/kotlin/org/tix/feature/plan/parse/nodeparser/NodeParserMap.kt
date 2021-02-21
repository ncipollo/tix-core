package org.tix.feature.plan.parse.nodeparser

import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.tix.feature.plan.parse.parseError

internal class NodeParserMap {
    private val headingParser = HeadingParser()
    private val map = mapOf(
        MarkdownElementTypes.ATX_1 to headingParser,
        MarkdownElementTypes.ATX_2 to headingParser,
        MarkdownElementTypes.ATX_3 to headingParser,
        MarkdownElementTypes.ATX_4 to headingParser,
        MarkdownElementTypes.ATX_5 to headingParser,
        MarkdownElementTypes.ATX_6 to headingParser
    ).mapKeys { it.key.toString() }

    fun parserForElementType(elementType: IElementType) =
        map[elementType.toString()] ?: parseError("Unknown element type: $elementType")
}