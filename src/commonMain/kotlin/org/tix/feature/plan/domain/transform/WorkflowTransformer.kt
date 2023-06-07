package org.tix.feature.plan.domain.transform

import org.tix.config.data.Workflow

fun Workflow.transform(variableMap: TransformVariableMap) =
    copy(actions = actions.map { it.transform(variableMap) })