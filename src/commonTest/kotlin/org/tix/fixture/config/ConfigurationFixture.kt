package org.tix.fixture.config

import org.tix.config.data.GithubConfiguration
import org.tix.config.data.JiraConfiguration
import org.tix.config.data.TixConfiguration
import org.tix.config.data.auth.AuthConfiguration
import org.tix.config.domain.TicketSystemAuth
import org.tix.serialize.dynamic.DynamicElement

//We need the custom getter here otherwise this fails in JS environments.
val authConfiguration
    get() = AuthConfiguration(
        username = "user",
        password = "password"
    )

val githubConfiguration
    get() = GithubConfiguration(
        auth = authConfiguration,
        owner = "owner",
        repo = "repo",
        noProjects = true,
        fields = githubFieldConfiguration
    )

val jiraConfiguration
    get() = JiraConfiguration(
        auth = authConfiguration,
        noEpics = true,
        fields = jiraFieldConfiguration,
        url = "url"
    )

val ticketSystemAuth
    get() = TicketSystemAuth(
        github = authConfiguration,
        jira = authConfiguration
    )

val tixConfiguration
    get() = TixConfiguration(
        include = DynamicElement("my_tix"),
        github = githubConfiguration,
        jira = jiraConfiguration,
        variables = mapOf("key" to "value")
    )
