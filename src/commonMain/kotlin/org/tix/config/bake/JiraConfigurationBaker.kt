package org.tix.config.bake

import org.tix.config.bake.validation.JiraConfigValidator
import org.tix.config.data.JiraConfiguration
import org.tix.config.data.auth.AuthConfiguration
import org.tix.config.data.raw.RawJiraConfiguration

object JiraConfigurationBaker {
    fun bake(config: RawJiraConfiguration, authConfig: AuthConfiguration): JiraConfiguration {
        JiraConfigValidator.validate(config)

        return JiraConfiguration(
            auth = authConfig,
            noEpics = config.noEpics ?: false,
            fields = config.fields,
            url = config.url!!
        )
    }
}