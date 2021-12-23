package org.tix.feature.plan.domain.parse.nodeparser

import org.tix.ticket.body.WhitespaceSegment

internal class WhitespaceParser : NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val count = arguments.currentNode.run { endOffset - startOffset }
        arguments.state.addBodySegments(WhitespaceSegment(count))
        return arguments.resultsFromArgs()
    }
}