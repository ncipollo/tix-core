package org.tix.test.platform

import org.tix.platform.Env

class TestEnv(vararg keyValues: Pair<String, String>) : Env {
    private val variables = keyValues.toMap()
    override fun get(name: String) = variables[name] ?: ""
}

fun testEnv(vararg keyValues: Pair<String, String>) = TestEnv(*keyValues)