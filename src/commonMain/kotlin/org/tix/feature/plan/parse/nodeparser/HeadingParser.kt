package org.tix.feature.plan.parse.nodeparser

internal class HeadingParser : NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        return arguments.resultsFromArgs(1)
    }
}