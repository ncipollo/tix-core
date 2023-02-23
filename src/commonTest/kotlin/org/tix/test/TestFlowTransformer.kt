package org.tix.test

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.tix.domain.FlowTransformer

class TestFlowTransformer<T, R>(vararg expectations: Pair<T, R>) : FlowTransformer<T, R> {
    private val expectationMap = expectations.toMap()
    override fun transformFlow(upstream: Flow<T>): Flow<R> =
        upstream.map {
            val result = expectationMap[it]
            result ?: error("unexpected input from upstream: $it")
        }
}

class TestErrorTransformer<T, R>(private val error: Throwable) : FlowTransformer<T, R> {
    override fun transformFlow(upstream: Flow<T>): Flow<R> =
        upstream.map {
            throw error
        }
}

fun <T, R> testTransformer(vararg expectations: Pair<T, R>) = TestFlowTransformer(*expectations)

fun <T, R> testErrorTransformer(error: Throwable) = TestErrorTransformer<T, R>(error)