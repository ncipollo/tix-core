package org.tix.feature.plan.parse.nodeparser

internal class LineBreakParser : NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        arguments.state.addBodyLineBreak()
        return arguments.resultsFromArgs(1)
    }
}