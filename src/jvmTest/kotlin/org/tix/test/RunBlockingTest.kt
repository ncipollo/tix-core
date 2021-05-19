package org.tix.test

import kotlinx.coroutines.runBlocking

actual fun runBlockingTest(block: suspend () -> Unit) = runBlocking { block() }