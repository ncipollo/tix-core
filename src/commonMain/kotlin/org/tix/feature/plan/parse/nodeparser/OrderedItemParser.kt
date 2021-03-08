package org.tix.feature.plan.parse.nodeparser

import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.tix.feature.plan.parse.parseError
import org.tix.model.ticket.body.OrderedListItemSegment
import org.tix.model.ticket.body.toTicketBody

internal class OrderedItemParser(private val parserMap: NodeParserMap): NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val level = arguments.state.listState.currentLevel
        val number = findNumber(arguments)
        val partialBody = parseFilteredChildren(arguments, parserMap) {
            it.type.name != MarkdownTokenTypes.LIST_NUMBER.name
        }

        val segment = OrderedListItemSegment(body = partialBody.toTicketBody(), level = level, number = number)
        arguments.state.addBodySegments(segment)

        return arguments.resultsFromArgs()
    }

    private fun findNumber(arguments: ParserArguments) : Int {
        val bulletNode = arguments.currentNode.findChildOfType(MarkdownTokenTypes.LIST_NUMBER)
            ?: parseError("no number found for list item", arguments)
        return bulletNode.getTextInNode(arguments.markdownText)
            .toString()
            .trim()
            .filterNot { it in listOf('.', ')') }
            .toInt()
    }
}