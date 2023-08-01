package org.tix.feature.plan.presentation

import org.tix.config.domain.ConfigurationSourceOptions
import org.tix.feature.plan.domain.parse.MarkdownFileSource
import org.tix.feature.plan.domain.parse.MarkdownTextSource
import kotlin.test.Test
import kotlin.test.assertEquals

class PlanViewEventTest {
    private companion object {
        const val CONFIG = "config"
        const val TITLE = "title"
    }

    @Test
    fun planUsingMarkdown() {
        val path = "/a/ticket.md"
        val expected = PlanViewEvent.PlanUsingMarkdown(
            markdownSource = MarkdownFileSource(path),
            configSourceOptions = ConfigurationSourceOptions(
                workspaceDirectory = "/a"
            ),
            shouldDryRun = true
        )
        val event = PlanViewEvent.PlanUsingMarkdown(
            markdownPath = path,
            shouldDryRun = true
        )
        assertEquals(expected, event)
    }

    @Test
    fun quickTicket_defaults() {
        val expected = PlanViewEvent.PlanUsingMarkdown(
            markdownSource = MarkdownTextSource("# title"),
        )
        val event = PlanViewEvent.quickTicket(TITLE)
        assertEquals(expected, event)
    }

    @Test
    fun quickTicket_withAllArguments() {
        val expected = PlanViewEvent.PlanUsingMarkdown(
            markdownSource = MarkdownTextSource("# title"),
            configSourceOptions = ConfigurationSourceOptions(savedConfigName = CONFIG),
            shouldDryRun = true
        )
        val event = PlanViewEvent.quickTicket(
            ticketTitle = TITLE,
            includedConfig = CONFIG,
            shouldDryRun = true
        )
        assertEquals(expected, event)
    }
}