package org.tix.feature.plan.domain.parse.nodeparser

import org.intellij.markdown.ast.ASTNode
import org.tix.feature.plan.domain.parse.traverseTickets

internal fun parseChildren(arguments: ParserArguments, parserMap: NodeParserMap) =
    arguments.state.buildNestedBody {
        arguments.childArguments?.let {
            traverseTickets(it, parserMap)
        }
    }

internal fun parseFilteredChildren(arguments: ParserArguments,
                                   parserMap: NodeParserMap,
                                   predicate: (ASTNode) -> Boolean) =
    arguments.state.buildNestedBody {
        arguments.filteredChildArguments(predicate)?.let {
            traverseTickets(it, parserMap)
        }
    }