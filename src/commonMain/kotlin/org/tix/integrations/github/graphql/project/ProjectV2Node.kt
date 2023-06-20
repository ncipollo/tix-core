package org.tix.integrations.github.graphql.project

import kotlinx.serialization.Serializable


@Serializable
data class ProjectV2Node(
    val id: String = "",
    val number: Long = 0,
    val title: String = "",
    val shortDescription: String? = null,
    val closed: Boolean = false
)
