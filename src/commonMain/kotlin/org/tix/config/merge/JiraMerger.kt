package org.tix.config.merge

import org.tix.config.data.raw.RawJiraConfiguration
import org.tix.config.data.raw.RawJiraFieldConfiguration

fun RawJiraConfiguration.merge(overlay: RawJiraConfiguration?) = RawJiraConfiguration(
    auth = overlay?.auth ?: auth,
    noEpics = overlay?.noEpics ?: noEpics,
    fields = fields.merge(overlay?.fields),
    url = overlay?.url ?: url
)

fun RawJiraFieldConfiguration.merge(overlay: RawJiraFieldConfiguration?) =
    overlay?.let {
        RawJiraFieldConfiguration(
            default = default + it.default,
            epic = epic + it.epic,
            issue = issue + it.issue,
            task = task + it.task
        )
    } ?: this