package org.tix.feature.plan.domain.transform

import org.tix.platform.Env

class TransformVariableMap(
    private val env: Env,
    variables: Map<String, String> = emptyMap()
) {
    private val standardizedVariables = variables.mapKeys { "$" + it.key.removePrefix("$") }

    val keys = standardizedVariables.keys

    operator fun get(key: String) =
        standardizedVariables[key]?.let { value ->
            if (value.startsWith("$")) envGet(value) else value
        } ?: ""

    private fun envGet(key: String) = env[key.removePrefix("$")]
}