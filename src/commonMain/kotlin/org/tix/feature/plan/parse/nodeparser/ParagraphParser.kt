package org.tix.feature.plan.parse.nodeparser

import org.tix.feature.plan.parse.traverseTickets
import org.tix.model.ticket.body.ParagraphSegment
import org.tix.model.ticket.body.toTicketBody

internal class ParagraphParser(private val parserMap: NodeParserMap) : NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val body = parseNestedBody(arguments)
        arguments.state.addBodySegments(ParagraphSegment(body = body.toTicketBody()))
        return arguments.resultsFromArgs()
    }

    private fun parseNestedBody(arguments: ParserArguments) =
        arguments.state.buildNestedBody {
            arguments.childArguments?.let {
                traverseTickets(it, parserMap)
            }
        }
}