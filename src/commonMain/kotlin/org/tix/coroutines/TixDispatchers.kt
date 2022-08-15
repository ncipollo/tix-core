package org.tix.coroutines

import kotlin.coroutines.CoroutineContext

interface TixDispatchers {
    val default: CoroutineContext
    val main: CoroutineContext
    val io: CoroutineContext
}