package org.tix.feature.plan.domain.parse.nodeparser

import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.tix.feature.plan.domain.parse.parseError
import org.tix.model.ticket.body.BulletListItemSegment
import org.tix.model.ticket.body.toTicketBody

internal class BulletItemParser(private val parserMap: NodeParserMap): NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val level = arguments.state.listState.currentLevel
        val marker = findMarkerText(arguments)
        val partialBody = parseFilteredChildren(arguments, parserMap) {
            it.type.name != MarkdownTokenTypes.LIST_BULLET.name
                    && it.type.name != MarkdownTokenTypes.WHITE_SPACE.name
        }

        val segment = BulletListItemSegment(body = partialBody.toTicketBody(), level = level, marker = marker)
        arguments.state.addBodySegments(segment)

        return arguments.resultsFromArgs()
    }

    private fun findMarkerText(arguments: ParserArguments) : String {
        val bulletNode = arguments.currentNode.findChildOfType(MarkdownTokenTypes.LIST_BULLET)
            ?: parseError("no bullet found for list item", arguments)
        return bulletNode.getTextInNode(arguments.markdownText)
            .toString()
            .trim()
    }
}