package org.tix.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RuntimeDispatchers: TixDispatchers {
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main

    override val io: CoroutineDispatcher = runtimeIODispatcher
}

internal expect val runtimeIODispatcher: CoroutineDispatcher