package org.tix.fixture.config

import org.tix.config.data.auth.AuthSource
import org.tix.config.data.raw.*
import org.tix.serialize.dynamic.DynamicElement

//We need the custom getter here otherwise this fails in JS environments.
val rawAction = RawAction(
    type = "action",
    arguments = DynamicElement(
        mapOf(
            "property" to "value",
            "nested" to mapOf(
                "deep" to "key"
            )
        )
    )
)

val rawWorkflows
    get() = RawTicketWorkflows(
        beforeAll = listOf(RawWorkflow(label = "before_all", actions = listOf(rawAction))),
        beforeEach = listOf(RawWorkflow(label = "before_each", actions = listOf(rawAction))),
        afterAll = listOf(RawWorkflow(label = "after_all", actions = listOf(rawAction))),
        afterEach = listOf(RawWorkflow(label = "after_each", actions = listOf(rawAction)))
    )

val rawGithubConfiguration
    get() = RawGithubConfiguration(
        fields = githubFieldConfig,
        noProjects = false,
        owner = "owner",
        repo = "repo",
        workflows = rawWorkflows
    )

val rawJiraConfiguration
    get() = RawJiraConfiguration(
        fields = jiraFieldConfig,
        noEpics = false,
        url = "https://api.example.com",
        workflows = rawWorkflows
    )

val rawTixConfiguration
    get() = RawTixConfiguration(
        include = DynamicElement("my_tix"),
        github = rawGithubConfiguration,
        jira = rawJiraConfiguration,
        variables = mapOf("key" to "value")
    )