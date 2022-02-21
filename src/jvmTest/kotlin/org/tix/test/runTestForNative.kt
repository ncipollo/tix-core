package org.tix.test

import kotlinx.coroutines.test.runTest

actual fun runTestWorkaround(testBody: suspend () -> Unit) {
    runTest { testBody() }
}