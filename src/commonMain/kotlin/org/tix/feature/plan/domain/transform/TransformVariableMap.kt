package org.tix.feature.plan.domain.transform

import org.tix.feature.plan.domain.transform.replace.TransformVariableMetadata
import org.tix.platform.Env

internal class TransformVariableMap(
    private val env: Env,
    variables: Map<String, String> = emptyMap(),
    val variableToken: String = "$"
) {
    private val standardizedVariables = variables.mapKeys { variableToken + it.key.removePrefix(variableToken) }

    val keys = standardizedVariables.keys

    val variableMetadata = TransformVariableMetadata(
        sortedNames = keys.sortedBy { it.length },
        longestNameLength = keys.maxOfOrNull { it.length } ?: 0,
        shortestNameLength = keys.minOfOrNull { it.length } ?: 0,
        variableToken = variableToken
    )

    operator fun get(key: String) =
        standardizedVariables[key]?.let { value ->
            if (value.startsWith(variableToken)) envGet(value) else value
        } ?: ""

    private fun envGet(key: String) = env[key.removePrefix(variableToken)]
}