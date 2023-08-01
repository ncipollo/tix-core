package org.tix.feature.plan.domain.parse

sealed interface MarkdownSource

data class MarkdownFileSource(val path: String) : MarkdownSource

data class MarkdownTextSource(val markdown: String) : MarkdownSource