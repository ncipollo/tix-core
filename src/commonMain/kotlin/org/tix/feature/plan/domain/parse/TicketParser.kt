package org.tix.feature.plan.domain.parse

import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.parser.MarkdownParser
import org.tix.feature.plan.domain.parse.nodeparser.NodeParserMap
import org.tix.feature.plan.domain.parse.nodeparser.parserArguments
import org.tix.model.ticket.Ticket

internal class TicketParser(private val markdownParser: MarkdownParser = defaultMarkdownParser()) {
    private val parserMap = NodeParserMap()

    fun parse(markdown: String): List<Ticket> {
        val rootNode = parseMarkdown(markdown)
        return parseNodes(rootNode, markdown)
    }

    private fun parseMarkdown(markdown: String) = markdownParser.buildMarkdownTreeFromString(markdown)

    private fun parseNodes(rootNode: ASTNode, markdown: String): List<Ticket> {
        val arguments = parserArguments(markdown, rootNode.children)
        traverseTickets(arguments, parserMap)
        arguments.state.completeAllTickets()
        return arguments.state.rootTickets.map { it.ticket() }
    }
}