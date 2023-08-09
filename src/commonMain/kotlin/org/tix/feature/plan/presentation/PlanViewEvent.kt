package org.tix.feature.plan.presentation

import org.tix.config.domain.ConfigurationSourceOptions
import org.tix.feature.plan.domain.parse.MarkdownFileSource
import org.tix.feature.plan.domain.parse.MarkdownSource
import org.tix.feature.plan.domain.parse.MarkdownTextSource

sealed class PlanViewEvent {
    companion object {
        fun quickTicket(
            ticketTitle: String,
            includedConfig: String? = null,
            workspaceDirectory: String? = null,
            shouldDryRun: Boolean = false
        ) = PlanUsingMarkdown(
            markdownSource = MarkdownTextSource("# $ticketTitle"),
            configSourceOptions = ConfigurationSourceOptions(
                workspaceDirectory = workspaceDirectory,
                savedConfigName = includedConfig
            ),
            shouldDryRun = shouldDryRun
        )
    }

    data class PlanUsingMarkdown(
        val markdownSource: MarkdownSource,
        val configSourceOptions: ConfigurationSourceOptions = ConfigurationSourceOptions(),
        val shouldDryRun: Boolean = false
    ) : PlanViewEvent() {
        constructor(
            markdownPath: String,
            shouldDryRun: Boolean
        ) : this(
            markdownSource = MarkdownFileSource(markdownPath),
            configSourceOptions = ConfigurationSourceOptions.forMarkdownSource(markdownPath),
            shouldDryRun = shouldDryRun
        )
    }
}
