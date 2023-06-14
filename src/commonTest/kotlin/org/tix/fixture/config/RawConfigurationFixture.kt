package org.tix.fixture.config

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

val rawGithubFieldConfig = RawGithubFieldConfiguration(
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
val rawGithubConfiguration
    get() = RawGithubConfiguration(
        fields = rawGithubFieldConfig,
        noProjects = false,
        owner = "owner",
        repo = "repo",
        workflows = rawWorkflows
    )

val rawJiraFieldConfig = RawJiraFieldConfiguration(
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

val rawJiraConfiguration
    get() = RawJiraConfiguration(
        fields = rawJiraFieldConfig,
        noEpics = false,
        url = "https://api.example.com",
        workflows = rawWorkflows
    )

val rawTixConfiguration
    get() = RawTixConfiguration(
        include = DynamicElement("my_tix"),
        github = rawGithubConfiguration,
        jira = rawJiraConfiguration,
        variables = mapOf("key" to "value"),
        variableToken = "**"
    )