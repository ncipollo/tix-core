package org.tix.config.data

import org.tix.config.data.auth.AuthConfiguration

data class JiraConfiguration(
    val auth: AuthConfiguration,
    val noEpics: Boolean,
    val fields: JiraFieldConfiguration,
    val url: String,
)