package org.tix.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

actual fun runTestWorkaround(testBody: suspend (CoroutineScope) -> Unit) {
    runBlocking { testBody(this) }
}