package org.tix.feature.plan.domain.transform

internal fun Map<String, Any?>.transform(variableMap: TransformVariableMap) =
    map {(key, value) ->
        key.transform(variableMap) to value.transform(variableMap)
    }.toMap()

private fun Any?.transform(variableMap: TransformVariableMap) =
    (this as? String)?.transform(variableMap) ?: this