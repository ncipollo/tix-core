package org.tix.ext

import org.tix.domain.FlowResult

fun <T> Result<T>.toFlowResult(): FlowResult<T> =
    if (isSuccess) {
        FlowResult.success(getOrThrow())
    } else {
        FlowResult.failure(exceptionOrNull())
    }