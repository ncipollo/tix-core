package org.tix.feature.plan.parse

import org.tix.feature.plan.parse.nodeparser.NodeParserMap
import org.tix.feature.plan.parse.nodeparser.ParserArguments

internal fun traverseTickets(initialArguments: ParserArguments, parserMap: NodeParserMap) {
    var arguments = initialArguments
    while (arguments.shouldContinueParsing) {
        val parser = parserMap.parserForNode(arguments.currentNode)
        val result = parser.parse(arguments)
        arguments = arguments.nextArgsFromResult(result)
    }
}