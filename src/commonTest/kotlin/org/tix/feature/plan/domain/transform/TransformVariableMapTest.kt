package org.tix.feature.plan.domain.transform

import org.tix.feature.plan.domain.transform.replace.TransformVariableMetadata
import org.tix.test.platform.testEnv
import kotlin.test.Test
import kotlin.test.expect

class TransformVariableMapTest {
    private val customTokenVariables = mapOf(
        "var1" to "value1",
        "**var2" to "value2",
        "env" to "\$envVar"
    )
    private val variables = mapOf(
        "var1" to "value1",
        "\$var2" to "value2",
        "env" to "\$envVar",
        "longer_var1" to "longer_value"
    )

    @Test
    fun keys() {
        val variableMap = TransformVariableMap(testEnv(), variables)
        expect(setOf("\$var1", "\$var2" , "\$env", "\$longer_var1")) { variableMap.keys }
    }

    @Test
    fun keys_customToken() {
        val variableMap = TransformVariableMap(testEnv(), customTokenVariables, "**")
        expect(setOf("**var1", "**var2" , "**env")) { variableMap.keys }
    }

    @Test
    fun get_configVariable() {
        val variableMap = TransformVariableMap(testEnv(), variables)
        expect("value1") { variableMap["\$var1"] }
        expect("value2") { variableMap["\$var2"] }
    }

    @Test
    fun get_configVariable_customToken() {
        val variableMap = TransformVariableMap(testEnv(), customTokenVariables, "**")
        expect("value1") { variableMap["**var1"] }
        expect("value2") { variableMap["**var2"] }
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

    @Test
    fun variableMetadata() {
        val env = testEnv()
        val variableMap = TransformVariableMap(env, variables)

        val expectedMetadata = TransformVariableMetadata(
            sortedNames = listOf("\$env", "\$var1", "\$var2", "\$longer_var1"),
            longestNameLength = "\$longer_var1".length,
            shortestNameLength = "\$env".length,
            variableToken = variableMap.variableToken
        )
        expect(expectedMetadata) { variableMap.variableMetadata }
    }

    @Test
    fun variableMetadata_emptyMap() {
        val env = testEnv()
        val variableMap = TransformVariableMap(env, emptyMap())

        val expectedMetadata = TransformVariableMetadata(
            sortedNames = emptyList(),
            longestNameLength = 0,
            shortestNameLength = 0,
            variableToken = variableMap.variableToken
        )
        expect(expectedMetadata) { variableMap.variableMetadata }
    }
}