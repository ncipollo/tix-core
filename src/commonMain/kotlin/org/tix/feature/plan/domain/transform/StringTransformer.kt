package org.tix.feature.plan.domain.transform

fun String.transform(variableMap: TransformVariableMap): String {
    val variableString = variableMap.keys.joinToString("|") { "\\$it" }
    val regex = variableString.toRegex()
    return replace(regex) { matchResult ->
        variableMap[matchResult.value]
    }
}
