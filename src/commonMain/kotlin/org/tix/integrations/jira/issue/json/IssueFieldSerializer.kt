package org.tix.integrations.jira.issue.json

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import org.tix.integrations.jira.issue.IssueFields

@OptIn(ExperimentalSerializationApi::class)
object IssueFieldSerializer : JsonTransformingSerializer<IssueFields>(IssueFields.serializer()) {
    private const val UNKNOWNS_KEY = "unknowns"
    private val knownKeys = descriptor.elementNames.toSet().filter { it != UNKNOWNS_KEY }

    override fun transformDeserialize(element: JsonElement): JsonElement =
        (element as? JsonObject)
            ?.let { transformDeserializeJsonObject(it) }
            ?: element

    private fun transformDeserializeJsonObject(jsonObject: JsonObject): JsonElement {
        val jsonMap = mutableMapOf<String, JsonElement>()
        val unknownsMap = mutableMapOf<String, JsonElement>()
        jsonObject.forEach { (key, element) ->
            if (knownKeys.contains(key)) {
                jsonMap[key] = element
            } else {
                unknownsMap[key] = element
            }
        }
        jsonMap[UNKNOWNS_KEY] = JsonObject(unknownsMap)

        return JsonObject(jsonMap)
    }

    override fun transformSerialize(element: JsonElement): JsonElement =
        (element as? JsonObject)
            ?.let { transformSerializeJsonObject(it) }
            ?: element

    private fun transformSerializeJsonObject(jsonObject: JsonObject): JsonElement {
        val jsonMap = jsonObject
            .flatMap { (key, element) ->
                if (key == "unknowns") {
                    flattenUnknownsObject(element)
                } else {
                    listOf(key to element)
                }
            }
            .toMap()
        return JsonObject(jsonMap)
    }

    private fun flattenUnknownsObject(element: JsonElement) =
        (element as? JsonObject)?.entries?.map { it.toPair() } ?: listOf("unknowns" to element)
}