package org.tix.fixture.config

import org.tix.config.data.auth.AuthSource
import org.tix.config.data.raw.RawAuthConfiguration
import org.tix.config.data.raw.RawGithubConfiguration
import org.tix.config.data.raw.RawJiraConfiguration
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.serialize.dynamic.DynamicElement

//We need the custom getter here otherwise this fails in JS environments.
val rawAuthConfiguration
    get() = RawAuthConfiguration(
        source = AuthSource.TIX_FILE,
        file = "my_tix"
    )

val rawGithubConfiguration
    get() = RawGithubConfiguration(
        fields = githubFieldConfiguration,
        noProjects = true,
        owner = "owner",
        repo = "repo"
    )

val rawJiraConfiguration
    get() = RawJiraConfiguration(
        fields = jiraFieldConfiguration,
        noEpics = true,
        url = "url"
    )

val rawTixConfiguration
    get() = RawTixConfiguration(
        include = DynamicElement("my_tix"),
        github = rawGithubConfiguration,
        jira = rawJiraConfiguration,
        variables = mapOf("key" to "value")
    )