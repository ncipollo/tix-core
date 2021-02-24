package org.tix.feature.plan.parse.nodeparser

import org.tix.model.ticket.body.WhitespaceSegment

internal class WhitespaceParser : NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val count = arguments.currentNode.run { endOffset - startOffset }
        arguments.state.addBodySegments(WhitespaceSegment(count))
        return arguments.resultsFromArgs()
    }
}