package org.tix.test

import kotlinx.coroutines.CoroutineScope

/**
 * Workaround for https://youtrack.jetbrains.com/issue/KTOR-3612
 * KTOR client native multithreading currently breaks runTest in Kotlin 1.6.0
 */
expect fun runTestWorkaround(testBody: suspend (CoroutineScope) -> Unit)