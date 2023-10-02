package org.tix.config.merge

import org.tix.config.data.raw.*
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.Test
import kotlin.test.expect

class TixMergerTest {
    private val configs = listOf(1, 2, 3).map { index ->
        RawTixConfiguration(
            include = DynamicElement("config$index"),
            github = RawGithubConfiguration(
                fields = RawGithubFieldConfiguration(
                    default = mapOf("github$index" to DynamicElement(index))
                )
            ).takeIf { index < 3 },
            jira = RawJiraConfiguration(
                fields = RawJiraFieldConfiguration(
                    default = mapOf("jira$index" to DynamicElement(index))
                )
            ).takeIf { index < 3 },
            matrix = mapOf("matrix_$index" to listOf(DynamicElement("value_$index"))),
            variables = mapOf("config$index" to "$index"),
            variableToken = "$index"
        )
    }

    @Test
    fun flatten() {
        val expected = RawTixConfiguration(
            include = DynamicElement("config3"),
            github = RawGithubConfiguration(
                fields = RawGithubFieldConfiguration(
                    default = mapOf(
                        "github1" to DynamicElement(1),
                        "github2" to DynamicElement(2)
                    )
                )
            ),
            jira = RawJiraConfiguration(
                fields = RawJiraFieldConfiguration(
                    default = mapOf(
                        "jira1" to DynamicElement(1),
                        "jira2" to DynamicElement(2)
                    )
                )
            ),
            matrix = mapOf(
                "matrix_1" to listOf(DynamicElement("value_1")),
                "matrix_2" to listOf(DynamicElement("value_2")),
                "matrix_3" to listOf(DynamicElement("value_3"))
            ),
            variables = mapOf(
                "config1" to "1",
                "config2" to "2",
                "config3" to "3"
            ),
            variableToken = "3"
        )

        expect(expected) { configs.flatten() }
    }
}