package org.tix.integrations.jira

import org.tix.config.data.JiraConfiguration
import org.tix.net.http.AuthMethod

fun jiraAuthMethod(configuration: JiraConfiguration) =
    AuthMethod.Basic(
        username = configuration.auth.username,
        password = configuration.auth.password
    )