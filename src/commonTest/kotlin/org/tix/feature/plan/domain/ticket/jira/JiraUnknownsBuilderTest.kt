package org.tix.feature.plan.domain.ticket.jira

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.tix.integrations.jira.field.Field
import org.tix.integrations.jira.field.FieldSchema
import kotlin.test.Test
import kotlin.test.expect

class JiraUnknownsBuilderTest {
    private companion object {
        val LABELS_FIELD = Field(id = "1", name = "labels")
        val SIMPLE_UNKNOWN = Field(id = "2", name = "simple_unknown")
        val VALUE_UNKNOWN = Field(id = "3", name = "value_unknown", schema = FieldSchema(type = "option"))
    }

    private val cache = JiraFieldCache(listOf(LABELS_FIELD, SIMPLE_UNKNOWN, VALUE_UNKNOWN))
    private val unknownsBuilder = JiraUnknownsBuilder(cache)

    @Test
    fun unknowns() {
        val fields = mapOf(
            "labels" to "a,b,c",
            "simple_unknown" to 42,
            "value_unknown" to true,
            "not_in_cache" to "missing"
        )

        val expected = JsonObject(
            mapOf(
                // labels should be filtered out since it is a known key.
                "2" to JsonPrimitive(42),
                "3" to JsonObject(mapOf("value" to JsonPrimitive(true))),
                "not_in_cache" to JsonPrimitive("missing")
            )
        )
        expect(expected) {
            unknownsBuilder.unknowns(fields)
        }
    }
}