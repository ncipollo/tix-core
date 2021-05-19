package org.tix.feature.plan.domain.parse.nodeparser

import org.tix.model.ticket.body.StrongEmphasisSegment

internal class StrongEmphasisParser: NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val text = arguments.findTextInCurrentNode()
        arguments.state.addBodySegments(StrongEmphasisSegment(text))
        return arguments.resultsFromArgs()
    }
}