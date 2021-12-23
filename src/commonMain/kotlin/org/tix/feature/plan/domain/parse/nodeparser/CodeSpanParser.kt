package org.tix.feature.plan.domain.parse.nodeparser

import org.tix.ticket.body.CodeSpanSegment

internal class CodeSpanParser: NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val text = arguments.findTextInCurrentNode()
        arguments.state.addBodySegments(CodeSpanSegment(text))
        return arguments.resultsFromArgs()
    }
}