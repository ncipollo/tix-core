package org.tix.feature.plan.parse.nodeparser

internal class ParagraphParser(private val parserMap: NodeParserMap): NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        return arguments.resultsFromArgs()
    }
}