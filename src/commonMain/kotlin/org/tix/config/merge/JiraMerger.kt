package org.tix.config.merge

import org.tix.config.data.JiraFieldConfiguration
import org.tix.config.data.raw.RawJiraConfiguration

fun RawJiraConfiguration.merge(overlay: RawJiraConfiguration) = RawJiraConfiguration(
    auth = overlay.auth ?: auth,
    noEpics = overlay.noEpics ?: noEpics,
    fields = fields.merge(overlay.fields),
    url = overlay.url ?: url
)

fun JiraFieldConfiguration.merge(overlay: JiraFieldConfiguration) = JiraFieldConfiguration(
    default = default + overlay.default,
    epic = epic + overlay.epic,
    issue = issue + overlay.issue,
    task = task + overlay.task
)