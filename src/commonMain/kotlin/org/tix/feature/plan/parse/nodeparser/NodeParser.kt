package org.tix.feature.plan.parse.nodeparser

internal interface NodeParser {
    fun parse(arguments: ParserArguments) : ParserResult
}