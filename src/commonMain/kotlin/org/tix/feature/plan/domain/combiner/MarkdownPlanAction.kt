package org.tix.feature.plan.domain.combiner

import org.tix.config.domain.ConfigurationSourceOptions
import org.tix.feature.plan.domain.parse.MarkdownFileSource
import org.tix.feature.plan.domain.parse.MarkdownSource

data class MarkdownPlanAction(
    val markdownSource: MarkdownSource,
    val configSourceOptions: ConfigurationSourceOptions = ConfigurationSourceOptions(),
    val shouldDryRun: Boolean = false
) {
    constructor(
        markdownPath: String,
        shouldDryRun: Boolean
    ) : this(
        markdownSource = MarkdownFileSource(markdownPath),
        configSourceOptions = ConfigurationSourceOptions.forMarkdownSource(markdownPath),
        shouldDryRun = shouldDryRun
    )
}