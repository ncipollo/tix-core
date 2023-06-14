package org.tix.feature.plan.domain.transform

import org.tix.config.data.Action

internal fun Action.transform(variableMap: TransformVariableMap) =
    copy(arguments = arguments.transform(variableMap))