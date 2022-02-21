package org.tix.test

import kotlinx.coroutines.runBlocking

actual fun runTestWorkaround(testBody: suspend () -> Unit) {
    runBlocking { testBody() }
}