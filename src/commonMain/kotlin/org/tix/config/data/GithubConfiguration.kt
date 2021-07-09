package org.tix.config.data

import org.tix.config.data.auth.AuthConfiguration

data class GithubConfiguration(
    val auth: AuthConfiguration,
    val owner: String,
    val repo: String,
    val noProjects: Boolean,
    val fields: GithubFieldConfiguration,
)

