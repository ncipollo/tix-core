package org.tix.integrations.jira.issue.json

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject
import org.tix.ext.toJsonPrimitive
import org.tix.integrations.jira.issue.Issue
import org.tix.integrations.jira.issue.IssueFields
import org.tix.integrations.jira.issue.IssueType
import org.tix.integrations.jira.project.Project
import org.tix.serialize.TixSerializers
import kotlin.test.Test
import kotlin.test.expect

class IssueFieldSerializerTest {
    private companion object {
        val JSON_TEXT = """
            {
                "fields": {
                    "issuetype": {
                        "name": "Story"
                    },
                    "project": {
                        "key": "TIX"
                    },
                    "description": "description",
                    "summary": "summary",
                    "field_1": "value_1",
                    "field_2": "value_2"
                }
            }
        """.trimIndent()
        val FIELDS = IssueFields(
            summary = "summary",
            description = "description",
            project = Project(key = "TIX"),
            type = IssueType(name = "Story"),
            unknowns = JsonObject(
                mapOf(
                    "field_1" to "value_1".toJsonPrimitive(),
                    "field_2" to "value_2".toJsonPrimitive(),
                )
            )
        )
        val ISSUE = Issue(fields = FIELDS)
    }

    private val json = TixSerializers.json()

    @Test
    fun deserialize() {
        expect(ISSUE) {
            json.decodeFromString(JSON_TEXT)
        }
    }

    @Test
    fun serialize() {
        expect(JSON_TEXT) {
            json.encodeToString(ISSUE)
        }
    }
}