package org.tix.feature.plan.domain.parse.nodeparser

internal interface NodeParser {
    fun parse(arguments: ParserArguments) : ParserResult
}