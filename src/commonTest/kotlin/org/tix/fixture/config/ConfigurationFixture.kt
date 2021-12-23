package org.tix.fixture.config

import org.tix.config.data.*
import org.tix.config.data.auth.AuthConfiguration
import org.tix.config.data.raw.RawTicketWorkflows
import org.tix.config.data.raw.RawWorkflow
import org.tix.config.domain.TicketSystemAuth
import org.tix.serialize.dynamic.DynamicElement

//We need the custom getter here otherwise this fails in JS environments.
val action
    get() = Action(
        type = "action",
        arguments = mapOf(
            "property" to "value",
            "nested" to mapOf(
                "deep" to "key"
            )
        )
    )

val workflows
    get() = TicketWorkflows(
        beforeAll = listOf(Workflow(label = "before_all", actions = listOf(action))),
        beforeEach = listOf(Workflow(label = "before_each", actions = listOf(action))),
        afterAll = listOf(Workflow(label = "after_all", actions = listOf(action))),
        afterEach = listOf(Workflow(label = "after_each", actions = listOf(action)))
    )

val authConfiguration
    get() = AuthConfiguration(
        username = "user",
        password = "password"
    )

val ticketSystemAuth
    get() = TicketSystemAuth(
        github = authConfiguration,
        jira = authConfiguration
    )

val githubFieldConfig = GithubFieldConfiguration(
    default = mapOf(
        "common" to DynamicElement("default"),
        "unique0" to DynamicElement("default"),
    ),
    project = mapOf(
        "common" to DynamicElement("project"),
        "unique1" to DynamicElement("project"),
    ),
    issue = mapOf(
        "common" to DynamicElement("issue"),
        "unique2" to DynamicElement("issue"),
    )
)

val githubConfig = GithubConfiguration(
    auth = authConfiguration,
    owner = "owner",
    repo = "repo",
    noProjects = false,
    fields = githubFieldConfig,
    workflows = workflows
)

val jiraFieldConfig = JiraFieldConfiguration(
    default = mapOf(
        "common" to DynamicElement("default"),
        "unique0" to DynamicElement("default"),
    ),
    epic = mapOf(
        "common" to DynamicElement("epic"),
        "unique1" to DynamicElement("epic"),
    ),
    issue = mapOf(
        "common" to DynamicElement("issue"),
        "unique2" to DynamicElement("issue"),
    ),
    task = mapOf(
        "common" to DynamicElement("task"),
        "unique3" to DynamicElement("task"),
    )
)

val jiraConfig = JiraConfiguration(
    auth = authConfiguration,
    noEpics = false,
    fields = jiraFieldConfig,
    url = "https://api.example.com",
    workflows = workflows
)

val tixConfiguration
    get() = TixConfiguration(
        include = DynamicElement("my_tix"),
        github = githubConfig,
        jira = jiraConfig,
        variables = mapOf("key" to "value")
    )
