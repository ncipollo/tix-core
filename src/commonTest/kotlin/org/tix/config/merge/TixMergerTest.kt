package org.tix.config.merge

import org.tix.config.data.GithubFieldConfiguration
import org.tix.config.data.JiraFieldConfiguration
import org.tix.config.data.raw.RawGithubConfiguration
import org.tix.config.data.raw.RawJiraConfiguration
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.Test
import kotlin.test.expect

class TixMergerTest {
    private val configs = listOf(1, 2, 3).map {
        RawTixConfiguration(
            include = DynamicElement("config$it"),
            github = RawGithubConfiguration(
                fields = GithubFieldConfiguration(
                    default = mapOf("github$it" to DynamicElement(it))
                )
            ),
            jira = RawJiraConfiguration(
                fields = JiraFieldConfiguration(
                    default = mapOf("jira$it" to DynamicElement(it))
                )
            ),
            variables = mapOf("config$it" to "$it")
        )
    }

    @Test
    fun flatten() {
        val expected = RawTixConfiguration(
            include = DynamicElement("config3"),
            github = RawGithubConfiguration(
                fields = GithubFieldConfiguration(
                    default = mapOf(
                        "github1" to DynamicElement(1),
                        "github2" to DynamicElement(2),
                        "github3" to DynamicElement(3),
                    )
                )
            ),
            jira = RawJiraConfiguration(
                fields = JiraFieldConfiguration(
                    default = mapOf(
                        "jira1" to DynamicElement(1),
                        "jira2" to DynamicElement(2),
                        "jira3" to DynamicElement(3),
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