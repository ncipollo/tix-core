package org.tix.feature.plan.domain.parse.nodeparser

import org.intellij.markdown.MarkdownTokenTypes
import org.tix.ticket.body.BlockQuoteSegment
import org.tix.ticket.body.toTicketBody

internal class BlockQuoteParser(private val parserMap: NodeParserMap) : NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val body = parseFilteredChildren(arguments, parserMap) {
            it.type.name != MarkdownTokenTypes.BLOCK_QUOTE.name
        }
        arguments.state.addBodySegments(BlockQuoteSegment(body = body.toTicketBody()))
        return arguments.resultsFromArgs()
    }
}