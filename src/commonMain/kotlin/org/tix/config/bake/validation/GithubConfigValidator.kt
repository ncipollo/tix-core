package org.tix.config.bake.validation

import org.tix.config.data.raw.RawGithubConfiguration

object GithubConfigValidator : ConfigValidator<RawGithubConfiguration> {
    override val configName = "Github Configuration"

    override fun missingProperties(config: RawGithubConfiguration): List<String> =
        mutableListOf<String>().apply {
            if (config.owner == null) add("owner")
            if (config.repo == null) add("repo")
        }
}