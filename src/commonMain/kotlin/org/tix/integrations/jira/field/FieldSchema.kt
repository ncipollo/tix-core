package org.tix.integrations.jira.field

import kotlinx.serialization.Serializable

@Serializable
data class FieldSchema(val type: String = "", val system: String = "")
