package org.tix.feature.plan.parse.nodeparser

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.tix.feature.plan.parse.parseError
import org.tix.feature.plan.parse.state.ParserState

internal class HeadingParser : NodeParser {
    private companion object {
        val levelMap = mapOf(
            MarkdownElementTypes.ATX_1 to 0,
            MarkdownElementTypes.ATX_2 to 1,
            MarkdownElementTypes.ATX_3 to 2,
            MarkdownElementTypes.ATX_4 to 3,
            MarkdownElementTypes.ATX_5 to 4,
            MarkdownElementTypes.ATX_6 to 5,
        ).mapKeys { it.key.name }
    }

    override fun parse(arguments: ParserArguments): ParserResult {
        val ticketTitle = ticketTitle(arguments)
        updateTicketState(arguments)
        arguments.state.currentTicket?.title = ticketTitle.toString()

        return results(arguments)
    }

    private fun ticketTitle(arguments: ParserArguments) =
        arguments.currentNode.run {
            val contentNode = findChildOfType(MarkdownTokenTypes.ATX_CONTENT)
            contentNode?.getTextInNode(arguments.markdownText) ?: parseError(
                "header is missing content",
                this,
                arguments.markdownText
            )
        }

    private fun updateTicketState(arguments: ParserArguments) {
        val node = arguments.currentNode
        val state = arguments.state
        val levelDifference = state.ticketLevel - node.level()

        if (levelDifference >= -1) {
            completeTickets(state, levelDifference)
            state.startTicket()
        } else {
            parseError("header can't be more than one level greater than previous header", node, arguments.markdownText)
        }
    }

    private fun completeTickets(state: ParserState, levelDifference: Int) =
        repeat(levelDifference + 1) { state.completeTicket() }

    private fun ASTNode.level() = levelMap[type.name]!!

    private fun results(arguments: ParserArguments) : ParserResult {
        val nextType = arguments.nextNode?.type?.name
        return if (nextType == MarkdownTokenTypes.EOL.name) {
            arguments.resultsFromArgs(2)
        } else {
            arguments.resultsFromArgs(1)
        }
    }
}