package org.tix.config.bake

import org.tix.config.bake.validation.GithubConfigValidator
import org.tix.config.data.GithubConfiguration
import org.tix.config.data.auth.AuthConfiguration
import org.tix.config.data.raw.RawGithubConfiguration

object GithubConfigurationBaker {
    fun bake(config: RawGithubConfiguration, authConfig: AuthConfiguration): GithubConfiguration {
        GithubConfigValidator.validate(config)

        return GithubConfiguration(
            auth = authConfig,
            owner = config.owner!!,
            repo = config.repo!!,
            noProjects = config.noProjects ?: false,
            fields = config.fields
        )
    }
}