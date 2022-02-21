package org.tix.feature.plan.domain.ticket.jira

import kotlinx.serialization.json.JsonObject
import org.tix.ext.toJsonPrimitive

class JiraUnknownsBuilder(
    private val fieldCache: JiraFieldCache
) {
    fun unknowns(fields: Map<String, Any?>) : JsonObject {
        val unknownFields = fields.filterKeys { it !in JiraTicketSystemFields.fields }
        val jsonElements = unknownFields.map { (fieldKey, fieldValue) -> unknownForField(fieldKey, fieldValue) }
        return JsonObject(jsonElements.toMap())
    }

    private fun unknownForField(fieldKey: String, fieldValue: Any?) =
        fieldCache[fieldKey]?.let {
            if (it.useValueKey()) {
                idToFieldValuePair(it.id, fieldValue)
            } else {
                idToValuePair(it.id, fieldValue)
            }
        } ?: keyToValuePair(fieldKey, fieldValue)

    private fun idToFieldValuePair(id: String, fieldValue: Any?) =
        id to JsonObject(mapOf("value" to fieldValue.toJsonPrimitive()))

    private fun idToValuePair(id: String, fieldValue: Any?) =
        id to fieldValue.toJsonPrimitive()

    private fun keyToValuePair(fieldKey: String, fieldValue: Any?) =
        (fieldKey to fieldValue.toJsonPrimitive())
}