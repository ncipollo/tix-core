package org.tix.feature.plan.domain.parse.nodeparser

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.tix.feature.plan.domain.parse.parseError
import org.tix.model.ticket.body.LinkSegment

internal class LinkParser : NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val destination = destination(arguments)
        val title = title(arguments)
        arguments.state.addBodySegments(LinkSegment(destination, title))
        return arguments.resultsFromArgs()
    }

    private fun destination(arguments: ParserArguments) =
        arguments.currentNode.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)
            ?.getTextInNode(arguments.markdownText)
            ?.toString()
            ?: parseError("link is missing destination", arguments)

    private fun title(arguments: ParserArguments) =
        arguments.currentNode.findChildOfType(MarkdownElementTypes.LINK_TEXT)
            ?.findChildOfType(MarkdownTokenTypes.TEXT)
            ?.getTextInNode(arguments.markdownText)
            ?.toString()
            ?: parseError("link is missing title", arguments)
}