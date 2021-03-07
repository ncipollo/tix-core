package org.tix.feature.plan.parse.nodeparser

import org.tix.feature.plan.parse.state.ListType

internal class ListItemParser(parserMap: NodeParserMap): NodeParser {
    private val bulletItemParser = BulletItemParser(parserMap)
    private val orderedItemParser = OrderedItemParser(parserMap)

    override fun parse(arguments: ParserArguments): ParserResult {
        val listState = arguments.state.listState
        return if (listState.currentType == ListType.BULLET) {
            bulletItemParser.parse(arguments)
        } else {
            orderedItemParser.parse(arguments)
        }
    }
}