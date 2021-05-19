package org.tix.feature.plan.domain.parse.nodeparser

import org.tix.feature.plan.domain.parse.state.ListType

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