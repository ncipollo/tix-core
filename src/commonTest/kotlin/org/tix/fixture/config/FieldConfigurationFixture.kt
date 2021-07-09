package org.tix.fixture.config

import org.tix.config.data.GithubFieldConfiguration
import org.tix.config.data.JiraFieldConfiguration
import org.tix.config.data.dynamic.DynamicProperty

//We need the custom getter here otherwise this fails in JS environments.
val githubFieldConfiguration get() = GithubFieldConfiguration(default = mapOf("key" to DynamicProperty(boolean = true)))

val jiraFieldConfiguration get() = JiraFieldConfiguration(default = mapOf("key" to DynamicProperty(boolean = true)))