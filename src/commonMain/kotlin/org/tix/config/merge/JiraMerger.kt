package org.tix.config.merge

import org.tix.config.data.JiraConfiguration
import org.tix.config.data.JiraFieldConfiguration

fun JiraConfiguration.merge(overlay: JiraConfiguration) = JiraConfiguration(
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