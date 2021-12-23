package org.tix.feature.plan.domain.parse.nodeparser

import org.tix.ticket.body.TextSegment

internal class TextParser: NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val text = arguments.textInCurrentNode()
        arguments.state.addBodySegments(TextSegment(text))
        return arguments.resultsFromArgs()
    }
}