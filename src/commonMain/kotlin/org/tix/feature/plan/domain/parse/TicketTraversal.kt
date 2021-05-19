package org.tix.feature.plan.domain.parse

import org.tix.feature.plan.domain.parse.nodeparser.NodeParserMap
import org.tix.feature.plan.domain.parse.nodeparser.ParserArguments

internal fun traverseTickets(initialArguments: ParserArguments, parserMap: NodeParserMap) {
    var arguments = initialArguments
    while (arguments.shouldContinueParsing) {
        val parser = parserMap.parserForNode(arguments.currentNode)
        val result = parser.parse(arguments)
        arguments = arguments.nextArgsFromResult(result)
    }
}