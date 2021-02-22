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
        updateTicketState(arguments)
        arguments.state.currentTicket?.title = ticketTitle(arguments)

        return results(arguments)
    }

    private fun updateTicketState(arguments: ParserArguments) {
        val node = arguments.currentNode
        val state = arguments.state
        val levelDifference = state.ticketLevel - node.level()

        if (levelDifference >= 0) {
            completeTickets(state, levelDifference)
            state.startTicket()
        } else {
            parseError("header can't be more than one level greater than previous header", node, arguments.markdownText)
        }
    }

    private fun completeTickets(state: ParserState, levelDifference: Int) =
        repeat(levelDifference) { state.completeTicket() }

    private fun ASTNode.level() = levelMap[type.name]!!

    private fun ticketTitle(arguments: ParserArguments) =
        arguments.currentNode.run {
            val contentNode = findContentNode(this)
            contentNode?.getTextInNode(arguments.markdownText)?.toString()
                ?: parseError(
                    "header is missing content",
                    this,
                    arguments.markdownText
                )
        }

    private fun findContentNode(node: ASTNode) =
        node.findChildOfType(MarkdownTokenTypes.ATX_CONTENT)?.findChildOfType(MarkdownTokenTypes.TEXT)

    private fun results(arguments: ParserArguments): ParserResult {
        val nextType = arguments.nextNode?.type?.name
        return if (nextType == MarkdownTokenTypes.EOL.name) {
            arguments.resultsFromArgs(2)
        } else {
            arguments.resultsFromArgs(1)
        }
    }
}