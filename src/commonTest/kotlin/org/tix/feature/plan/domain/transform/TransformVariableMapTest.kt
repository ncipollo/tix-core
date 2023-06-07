package org.tix.feature.plan.domain.transform

import org.tix.test.platform.testEnv
import kotlin.test.Test
import kotlin.test.expect

class TransformVariableMapTest {
    private val variables = mapOf(
        "var1" to "value1",
        "\$var2" to "value2",
        "env" to "\$envVar"
    )

    @Test
    fun keys() {
        val variableMap = TransformVariableMap(testEnv(), variables)
        expect(setOf("\$var1", "\$var2" , "\$env")) { variableMap.keys }
    }

    @Test
    fun get_configVariable() {
        val variableMap = TransformVariableMap(testEnv(), variables)
        expect("value1") { variableMap["\$var1"] }
        expect("value2") { variableMap["\$var2"] }
    }

    @Test
    fun get_envVariable_missingFromEnvironment() {
        val env = testEnv()
        val variableMap = TransformVariableMap(env, variables)
        expect("") { variableMap["env"] }
    }

    @Test
    fun get_envVariable_presentInEnvironment() {
        val env = testEnv("envVar" to "value3")
        val variableMap = TransformVariableMap(env, variables)
        expect("value3") { variableMap["\$env"] }
    }
}