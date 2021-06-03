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

fun <T, R> testTransformer(vararg expectations: Pair<T, R>) = TestFlowTransformer(*expectations)