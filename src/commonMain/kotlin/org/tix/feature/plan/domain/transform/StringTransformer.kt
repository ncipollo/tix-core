package org.tix.feature.plan.domain.transform

import org.tix.feature.plan.domain.transform.replace.replaceVariables

internal fun String.transform(variableMap: TransformVariableMap) = replaceVariables(variableMap)
