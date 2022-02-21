package org.tix.config.bake

import org.tix.config.data.GithubFieldConfiguration
import org.tix.config.data.raw.RawGithubFieldConfiguration

object GithubConfigurationFieldBaker {
    fun bake(config: RawGithubFieldConfiguration) = GithubFieldConfiguration(
        default = config.default.mapValues { it.value.value },
        project = config.project.mapValues { it.value.value },
        issue = config.issue.mapValues { it.value.value }
    )
}