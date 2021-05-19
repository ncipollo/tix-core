package org.tix.feature.plan.domain.parse

import org.intellij.markdown.ast.ASTNode

fun ASTNode.lineNumber(markdownText: String) =
    markdownText.substring(0..startOffset).count { it == '\n' }