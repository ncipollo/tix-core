package org.tix.integrations.github.rest.sort

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SortDirection {
    @SerialName("asc") ASC,
    @SerialName("desc") DESC
}