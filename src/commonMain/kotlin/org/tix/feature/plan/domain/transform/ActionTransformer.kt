package org.tix.feature.plan.domain.transform

import org.tix.config.data.Action

fun Action.transform(variableMap: TransformVariableMap) =
    copy(arguments = arguments.transform(variableMap))