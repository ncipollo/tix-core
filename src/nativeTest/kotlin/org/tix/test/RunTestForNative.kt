package org.tix.test

import kotlinx.coroutines.runBlocking
import org.tix.coroutines.TestDispatchers

actual fun runTestWorkaround(testBody: suspend () -> Unit) {
    runBlocking {
        TestDispatchers(coroutineContext)
        testBody()
    }
}