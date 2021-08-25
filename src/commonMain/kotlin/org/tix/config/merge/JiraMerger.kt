package org.tix.config.merge

import org.tix.config.data.JiraFieldConfiguration
import org.tix.config.data.raw.RawJiraConfiguration

fun RawJiraConfiguration.merge(overlay: RawJiraConfiguration?) = RawJiraConfiguration(
    auth = overlay?.auth ?: auth,
    noEpics = overlay?.noEpics ?: noEpics,
    fields = fields.merge(overlay?.fields),
    url = overlay?.url ?: url
)

fun JiraFieldConfiguration.merge(overlay: JiraFieldConfiguration?) =
    overlay?.let {
        JiraFieldConfiguration(
            default = default + it.default,
            epic = epic + it.epic,
            issue = issue + it.issue,
            task = task + it.task
        )
    } ?: this