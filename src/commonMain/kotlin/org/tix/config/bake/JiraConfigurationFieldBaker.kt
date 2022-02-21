package org.tix.config.bake

import org.tix.config.data.JiraFieldConfiguration
import org.tix.config.data.raw.RawJiraFieldConfiguration

object JiraConfigurationFieldBaker {
    fun bake(config: RawJiraFieldConfiguration) = JiraFieldConfiguration(
        default = config.default.mapValues { it.value.value },
        epic = config.epic.mapValues { it.value.value },
        issue = config.issue.mapValues { it.value.value },
        task = config.task.mapValues { it.value.value }
    )
}