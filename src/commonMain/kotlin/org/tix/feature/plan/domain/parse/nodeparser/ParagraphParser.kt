package org.tix.feature.plan.domain.parse.nodeparser

import org.tix.model.ticket.body.ParagraphSegment
import org.tix.model.ticket.body.toTicketBody

internal class ParagraphParser(private val parserMap: NodeParserMap) : NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val body = parseChildren(arguments, parserMap)
        arguments.state.addBodySegments(ParagraphSegment(body = body.toTicketBody()))
        return arguments.resultsFromArgs()
    }
}