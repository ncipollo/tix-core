package org.tix.integrations.jira.field

import kotlinx.serialization.Serializable

@Serializable
data class Field(
    val id: String = "",
    val key: String = "",
    val name: String = "",
    val custom: Boolean = false,
    val navigable: Boolean = false,
    val searchable: Boolean = false,
    val clausNames: List<String> = emptyList(),
    val schema: FieldSchema = FieldSchema()
)