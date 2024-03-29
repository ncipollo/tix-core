package org.tix.feature.plan.domain.parse.nodeparser

import org.tix.ticket.body.ThematicBreakSegment

internal class ThematicBreakParser: NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        arguments.state.addBodySegments(ThematicBreakSegment)
        return arguments.resultsFromArgs()
    }
}