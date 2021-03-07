package org.tix.feature.plan.parse.nodeparser

import org.tix.model.ticket.body.BulletListSegment
import org.tix.model.ticket.body.toTicketBody

internal class BulletListParser(private val parserMap: NodeParserMap): NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val listState = arguments.state.listState
        listState.buildBulletList {
            val body = parseChildren(arguments, parserMap)
            val segment = BulletListSegment(body = body.toTicketBody(), level = listState.currentLevel)
            arguments.state.addBodySegments(segment)
        }
        return arguments.resultsFromArgs()
    }
}