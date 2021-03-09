package org.tix.feature.plan.parse.nodeparser

import org.tix.model.ticket.body.ThematicBreakSegment

internal class ThematicBreakParser: NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        arguments.state.addBodySegments(ThematicBreakSegment)
        return arguments.resultsFromArgs()
    }
}