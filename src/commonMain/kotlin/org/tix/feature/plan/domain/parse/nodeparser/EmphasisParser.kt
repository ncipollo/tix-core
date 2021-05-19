package org.tix.feature.plan.domain.parse.nodeparser

import org.tix.model.ticket.body.EmphasisSegment

internal class EmphasisParser: NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val text = arguments.findTextInCurrentNode()
        arguments.state.addBodySegments(EmphasisSegment(text))
        return arguments.resultsFromArgs()
    }
}