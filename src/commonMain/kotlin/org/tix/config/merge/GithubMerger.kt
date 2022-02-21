package org.tix.config.merge

import org.tix.config.data.raw.RawGithubConfiguration
import org.tix.config.data.raw.RawGithubFieldConfiguration

fun RawGithubConfiguration.merge(overlay: RawGithubConfiguration?) = RawGithubConfiguration(
    auth = overlay?.auth ?: auth,
    owner = overlay?.owner ?: owner,
    repo = overlay?.repo ?: repo,
    noProjects = overlay?.noProjects ?: noProjects,
    fields = fields.merge(overlay?.fields)
)

fun RawGithubFieldConfiguration.merge(overlay: RawGithubFieldConfiguration?) =
    overlay?.let {
        RawGithubFieldConfiguration(
            default = default + it.default,
            project = project + it.project,
            issue = issue + it.issue
        )
    } ?: this