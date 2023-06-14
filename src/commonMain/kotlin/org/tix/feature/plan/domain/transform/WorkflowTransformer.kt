package org.tix.feature.plan.domain.transform

import org.tix.config.data.Workflow

internal fun Workflow.transform(variableMap: TransformVariableMap) =
    copy(actions = actions.map { it.transform(variableMap) })