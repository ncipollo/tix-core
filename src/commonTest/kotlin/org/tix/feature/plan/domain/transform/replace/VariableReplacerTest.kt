package org.tix.feature.plan.domain.transform.replace

import org.tix.feature.plan.domain.transform.TransformVariableMap
import org.tix.test.platform.testEnv
import kotlin.test.Test
import kotlin.test.expect

class VariableReplacerTest {
    private val env = testEnv()
    private val variables = mapOf(
        "var1" to "value1",
        "var2" to "value2",
        "ticket.parent.id" to "parent"
    )

    @Test
    fun replaceVariables_noVariables_emptyVariableMap() {
        val variableMap = TransformVariableMap(env, emptyMap())
        val inputString = "\$var1, \$var2, \$ticket.parent.id"

        expect(inputString) {
            inputString.replaceVariables(variableMap)
        }
    }

    @Test
    fun replaceVariables_noVariables_noTokens() {
        val variableMap = TransformVariableMap(env, variables)
        val inputString = "no variables in this string"

        expect(inputString) {
            inputString.replaceVariables(variableMap)
        }
    }

    @Test
    fun replaceVariables_noVariables_noVariableMath() {
        val variableMap = TransformVariableMap(env, variables)
        val inputString = "before \$var3 after"

        expect(inputString) {
            inputString.replaceVariables(variableMap)
        }
    }

    @Test
    fun replaceVariables_multipleVariables_allInARow() {
        val variableMap = TransformVariableMap(env, variables)
        val inputString = "\$var1\$var2\$ticket.parent.id"

        expect("value1value2parent") {
            inputString.replaceVariables(variableMap)
        }
    }

    @Test
    fun replaceVariables_multipleVariables_badVariableThenGood() {
        val variableMap = TransformVariableMap(env, variables)
        val inputString = "\$va\$var1"

        expect("\$vavalue1") {
            inputString.replaceVariables(variableMap)
        }
    }

    @Test
    fun replaceVariables_multipleVariables_spacedOut() {
        val variableMap = TransformVariableMap(env, variables)
        val inputString = "\$var1, \$var2, \$ticket.parent.id"

        expect("value1, value2, parent") {
            inputString.replaceVariables(variableMap)
        }
    }

    @Test
    fun replaceVariables_noVariables_singleToken() {
        val variableMap = TransformVariableMap(env, variables)
        val inputString = "\$"

        expect(inputString) {
            inputString.replaceVariables(variableMap)
        }
    }

    @Test
    fun replaceVariables_withVariables_singleVariable_atStart() {
        val variableMap = TransformVariableMap(env, variables)
        val inputString = "\$var1 is what var1 is"

        expect("value1 is what var1 is") {
            inputString.replaceVariables(variableMap)
        }
    }

    @Test
    fun replaceVariables_withVariables_singleVariable_inMiddle() {
        val variableMap = TransformVariableMap(env, variables)
        val inputString = "before \$var1 after"

        expect("before value1 after") {
            inputString.replaceVariables(variableMap)
        }
    }

    @Test
    fun replaceVariables_withVariables_singleVariable_atEnd() {
        val variableMap = TransformVariableMap(env, variables)
        val inputString = "var1 is \$var1"

        expect("var1 is value1") {
            inputString.replaceVariables(variableMap)
        }
    }

    @Test
    fun replaceVariables_withVariables_singleVariable_strayTokens() {
        val variableMap = TransformVariableMap(env, variables)
        val inputString = "\$\$var1\$"

        expect("\$value1\$") {
            inputString.replaceVariables(variableMap)
        }
    }
}