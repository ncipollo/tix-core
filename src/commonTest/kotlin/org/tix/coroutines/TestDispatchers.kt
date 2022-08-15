package org.tix.coroutines

import kotlin.coroutines.CoroutineContext

class TestDispatchers(testDispatcher: CoroutineContext): TixDispatchers {
    override val default: CoroutineContext = testDispatcher
    override val main: CoroutineContext = testDispatcher
    override val io: CoroutineContext = testDispatcher
}