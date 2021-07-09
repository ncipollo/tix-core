package org.tix.config.merge

import org.tix.config.data.*
import org.tix.config.data.dynamic.DynamicProperty
import org.tix.config.data.raw.RawGithubConfiguration
import org.tix.config.data.raw.RawJiraConfiguration
import org.tix.config.data.raw.RawTixConfiguration
import kotlin.test.Test
import kotlin.test.expect

class TixMergerTest {
    private val configs = listOf(1, 2, 3).map {
        RawTixConfiguration(
            include = DynamicProperty(string = "config$it"),
            github = RawGithubConfiguration(
                fields = GithubFieldConfiguration(
                    default = mapOf(
                        "github$it" to DynamicProperty(
                            number = it
                        )
                    )
                )
            ),
            jira = RawJiraConfiguration(
                fields = JiraFieldConfiguration(
                    default = mapOf(
                        "jira$it" to DynamicProperty(
                            number = it
                        )
                    )
                )
            ),
            variables = mapOf("config$it" to "$it")
        )
    }

    @Test
    fun flatten() {
        val expected = RawTixConfiguration(
            include = DynamicProperty(string = "config3"),
            github = RawGithubConfiguration(
                fields = GithubFieldConfiguration(
                    default = mapOf(
                        "github1" to DynamicProperty(number = 1),
                        "github2" to DynamicProperty(number = 2),
                        "github3" to DynamicProperty(number = 3),
                    )
                )
            ),
            jira = RawJiraConfiguration(
                fields = JiraFieldConfiguration(
                    default = mapOf(
                        "jira1" to DynamicProperty(number = 1),
                        "jira2" to DynamicProperty(number = 2),
                        "jira3" to DynamicProperty(number = 3),
                    )
                )
            ),
            variables = mapOf(
                "config1" to "1",
                "config2" to "2",
                "config3" to "3"
            )
        )

        expect(expected) { configs.flatten() }
    }
}