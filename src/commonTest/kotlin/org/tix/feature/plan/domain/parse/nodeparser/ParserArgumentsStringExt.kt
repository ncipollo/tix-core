package org.tix.feature.plan.domain.parse.nodeparser

import org.tix.feature.plan.domain.parse.defaultMarkdownParser

internal fun String.toParserArguments() =
    parserArguments(this, defaultMarkdownParser().buildMarkdownTreeFromString(this).children)