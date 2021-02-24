package org.tix.feature.plan.parse.nodeparser

import org.tix.feature.plan.parse.traverseTickets

internal class ParagraphParser(private val parserMap: NodeParserMap): NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        arguments.state.buildNestedBody {
            arguments.childArguments?.let {
                traverseTickets(it, parserMap)
            }
        }
        return arguments.resultsFromArgs()
    }
}