package org.tix.feature.plan.domain.parse.nodeparser

internal class LineBreakParser : NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        arguments.state.addBodyLinebreak()
        return arguments.resultsFromArgs(1)
    }
}