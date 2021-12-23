package org.tix.test

import kotlinx.coroutines.test.runTest

actual fun runTestForNative(testBody: suspend () -> Unit) {
    runTest { testBody() }
}