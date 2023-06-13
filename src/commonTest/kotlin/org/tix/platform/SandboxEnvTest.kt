package org.tix.platform

import org.tix.test.platform.testEnv
import kotlin.test.Test
import kotlin.test.expect

class SandboxEnvTest {
    private val wrappedEnv = testEnv(
        "env1" to "value1",
        "env2" to "value2"
    )

    @Test
    fun get_allValuesAccepted() {
        val sandboxEnv = SandboxEnv(wrappedEnv) { true }
        expect(listOf("value1", "value2")) {
            listOf("env1", "env2").map { sandboxEnv[it] }
        }
    }

    @Test
    fun get_allValuesRejected() {
        val sandboxEnv = SandboxEnv(wrappedEnv) { false }
        expect(listOf("", "")) {
            listOf("env1", "env2").map { sandboxEnv[it] }
        }
    }

    @Test
    fun get_someValuesAccepted() {
        val sandboxEnv = SandboxEnv(wrappedEnv) { it == "env1" }
        expect(listOf("value1", "")) {
            listOf("env1", "env2").map { sandboxEnv[it] }
        }
    }
}