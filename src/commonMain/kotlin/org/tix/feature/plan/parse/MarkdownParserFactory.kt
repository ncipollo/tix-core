package org.tix.feature.plan.parse

import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

internal fun defaultMarkdownParser() = MarkdownParser(GFMFlavourDescriptor())