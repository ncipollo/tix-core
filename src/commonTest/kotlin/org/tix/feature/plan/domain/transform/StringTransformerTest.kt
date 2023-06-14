package org.tix.feature.plan.domain.transform

import org.tix.feature.plan.domain.ticket.MockTicketPlanResult
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.test.platform.testEnv
import kotlin.test.Test
import kotlin.test.expect

class StringTransformerTest {
    private  val env = testEnv()

    @Test
    fun transform_emptyVariables() {
        val context = PlanningContext<MockTicketPlanResult>()
        val inputString = "test \$var1 \$var2"
        expect(inputString) {
            inputString.transform(TransformVariableMap(env, context.variables))
        }
    }

    @Test
    fun transform_replacesAllVariables() {
        val context = PlanningContext<MockTicketPlanResult>(
            variables = mapOf(
                "var1" to "value1",
                "var2" to "value2",
                "ticket.parent.id" to "parent"
            )
        )
        val inputString = "test \$var1 \$var2 \$ticket.parent.id"
        expect("test value1 value2 parent") {
            inputString.transform(TransformVariableMap(env, context.variables))
        }
    }

    @Test
    fun transform_replacesAllVariables_customToken() {
        val context = PlanningContext<MockTicketPlanResult>(
            variables = mapOf(
                "var1" to "value1",
                "var2" to "value2",
                "ticket.parent.id" to "parent"
            )
        )
        val inputString = "test **var1 **var2 **ticket.parent.id"
        expect("test value1 value2 parent") {
            inputString.transform(TransformVariableMap(env, context.variables, "**"))
        }
    }

    @Test
    fun transform_replacesAllVariables_manyVariables() {
        val variables = (0 until 100).associate {
            "var$it" to "value$it"
        }
        val context = PlanningContext<MockTicketPlanResult>(variables = variables)

        val varNames = variables.keys.sorted().map { "\$$it" }
        val varValues = variables.keys.sorted().map { variables[it] }
        val inputString = "test $varNames"
        val expected = "test $varValues"
        expect(expected) {
            inputString.transform(TransformVariableMap(env, context.variables))
        }
    }

    @Test
    fun transform_replacesVariablesWhichArePresent() {
        val context = PlanningContext<MockTicketPlanResult>(
            variables = mapOf(
                "var1" to "value1",
                "var3" to "value3"
            )
        )
        val inputString = "test \$var1 \$var2"
        expect("test value1 \$var2") {
            inputString.transform(TransformVariableMap(env, context.variables))
        }
    }
}