package org.tix.config.merge

import org.tix.config.data.GithubFieldConfiguration
import org.tix.config.data.raw.RawGithubConfiguration

fun RawGithubConfiguration.merge(overlay: RawGithubConfiguration) = RawGithubConfiguration(
    auth = overlay.auth ?: auth,
    owner = overlay.owner ?: owner,
    repo = overlay.repo ?: repo,
    noProjects = overlay.noProjects ?: noProjects,
    fields = fields.merge(overlay.fields)
)

fun GithubFieldConfiguration.merge(overlay: GithubFieldConfiguration) = GithubFieldConfiguration(
    default = default + overlay.default,
    project = project + overlay.project,
    issue = issue + overlay.issue
)