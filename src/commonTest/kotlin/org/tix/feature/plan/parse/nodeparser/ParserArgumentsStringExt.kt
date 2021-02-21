package org.tix.feature.plan.parse.nodeparser

import org.tix.feature.plan.parse.defaultMarkdownParser

internal fun String.toParserArguments() =
    parserArguments(this, defaultMarkdownParser().buildMarkdownTreeFromString(this).children)