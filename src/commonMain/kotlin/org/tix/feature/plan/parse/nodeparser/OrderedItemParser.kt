package org.tix.feature.plan.parse.nodeparser

internal class OrderedItemParser(private val parserMap: NodeParserMap): NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        return arguments.resultsFromArgs()
    }
}