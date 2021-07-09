package org.tix.config.bake.validation

import org.tix.config.data.raw.RawJiraConfiguration

object JiraConfigValidator : ConfigValidator<RawJiraConfiguration> {
    override val configName = "Jira Configuration"

    override fun missingProperties(config: RawJiraConfiguration): List<String> =
        mutableListOf<String>().apply {
            if (config.url == null) add("jira url")
        }
}