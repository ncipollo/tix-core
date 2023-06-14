package org.tix.feature.plan.domain.transform.replace

internal data class TransformVariableMetadata(
    val sortedNames: List<String>,
    val longestNameLength: Int,
    val shortestNameLength: Int,
    val variableToken: String
)