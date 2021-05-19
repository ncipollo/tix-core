package org.tix.feature.plan.domain.parse.nodeparser

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.tix.model.ticket.body.BulletListSegment
import org.tix.model.ticket.body.OrderedListSegment
import org.tix.model.ticket.body.toTicketBody

internal class ListParser(private val parserMap: NodeParserMap) : NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val type = arguments.currentNode.type
        if (type.name == MarkdownElementTypes.UNORDERED_LIST.name) {
            parseBulletList(arguments)
        } else {
            parseOrderedList(arguments)
        }
        return arguments.resultsFromArgs()
    }

    private fun parseBulletList(arguments: ParserArguments) {
        val listState = arguments.state.listState
        listState.buildBulletList {
            val body = parseBody(arguments)
            val segment = BulletListSegment(body = body.toTicketBody(), level = listState.currentLevel)
            arguments.state.addBodySegments(segment)
        }
    }

    private fun parseOrderedList(arguments: ParserArguments) {
        val listState = arguments.state.listState
        listState.buildOrderedList {
            val body = parseBody(arguments)
            val segment = OrderedListSegment(body = body.toTicketBody(), level = listState.currentLevel)
            arguments.state.addBodySegments(segment)
        }
    }

    private fun parseBody(arguments: ParserArguments) =
        parseFilteredChildren(arguments, parserMap) {
            it.type.name != MarkdownTokenTypes.WHITE_SPACE.name
        }
}