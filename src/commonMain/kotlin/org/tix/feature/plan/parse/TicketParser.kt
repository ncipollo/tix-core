package org.tix.feature.plan.parse

import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.parser.MarkdownParser
import org.tix.feature.plan.parse.nodeparser.NodeParserMap
import org.tix.feature.plan.parse.nodeparser.parserArguments
import org.tix.model.ticket.Ticket

internal class TicketParser(private val markdownParser: MarkdownParser = defaultMarkdownParser()) {
    private val parserMap = NodeParserMap()

    fun parse(markdown: String): List<Ticket> {
        val rootNode = parseMarkdown(markdown)
        parseNodes(rootNode, markdown)
        return emptyList()
    }

    private fun parseMarkdown(markdown: String) = markdownParser.buildMarkdownTreeFromString(markdown)

    private fun parseNodes(rootNode: ASTNode, markdown: String) {
        var arguments = parserArguments(markdown, rootNode.children)

        while (arguments.shouldContinueParsing) {
            val parser = parserMap.parserForNode(arguments.currentNode, arguments.markdownText)
            val result = parser.parse(arguments)
            arguments = arguments.nextArgsFromResult(result)
        }
    }
}