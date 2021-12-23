package org.tix.test

import kotlinx.coroutines.runBlocking

actual fun runTestForNative(testBody: suspend () -> Unit) {
    runBlocking { testBody() }
}