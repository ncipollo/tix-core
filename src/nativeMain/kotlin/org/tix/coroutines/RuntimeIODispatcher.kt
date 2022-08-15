package org.tix.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.newSingleThreadContext

internal actual val runtimeIODispatcher: CoroutineDispatcher = newSingleThreadContext("io-dispatcher")
