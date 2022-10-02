package org.tix.feature.plan.domain.parse

import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.parser.MarkdownParser
import org.tix.config.data.TixConfiguration
import org.tix.feature.plan.domain.parse.nodeparser.NodeParserMap
import org.tix.feature.plan.domain.parse.nodeparser.parserArguments
import org.tix.ticket.Ticket

internal class TicketParser(private val markdownParser: MarkdownParser = defaultMarkdownParser()) {
    private val parserMap = NodeParserMap()
    private val idGenerator = TixIdGenerator()

    fun parse(arguments: TicketParserArguments): List<Ticket> {
        val rootNode = parseMarkdown(arguments.markdown)
        val tickets = parseNodes(rootNode, arguments.markdown)
        return idGenerator.attachIdsToTickets(tickets)
    }

    private fun parseMarkdown(markdown: String) = markdownParser.buildMarkdownTreeFromString(markdown)

    private fun parseNodes(rootNode: ASTNode, markdown: String): List<Ticket> {
        val arguments = parserArguments(markdown, rootNode.children)
        traverseTickets(arguments, parserMap)
        arguments.state.completeAllTickets()
        return arguments.state.rootTickets.map { it.ticket() }
    }
}

data class TicketParserArguments(val markdown: String = "", val configuration: TixConfiguration)