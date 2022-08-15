package org.tix.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest

actual fun runTestWorkaround(testBody: suspend (CoroutineScope) -> Unit) {
    runTest { testBody(this) }
}