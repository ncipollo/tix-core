package org.tix.domain

import kotlinx.coroutines.flow.Flow

interface FlowTransformer<T, R> {
    fun transformFlow(upstream: Flow<T>): Flow<R>
}

fun <T,R> Flow<T>.transform(transformer: FlowTransformer<T,R>) = transformer.transformFlow(this)