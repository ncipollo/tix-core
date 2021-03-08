package org.tix.feature.plan.parse.nodeparser

import org.tix.model.ticket.body.CodeSpanSegment

internal class CodeSpanParser: NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val text = arguments.findTextInCurrentNode()
        arguments.state.addBodySegments(CodeSpanSegment(text))
        return arguments.resultsFromArgs()
    }
}