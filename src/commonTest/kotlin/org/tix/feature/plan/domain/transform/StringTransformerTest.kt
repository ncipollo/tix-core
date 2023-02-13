package org.tix.feature.plan.domain.transform

import org.tix.feature.plan.domain.ticket.MockTicketPlanResult
import org.tix.feature.plan.domain.ticket.PlanningContext
import kotlin.test.Test
import kotlin.test.expect

class StringTransformerTest {
    @Test
    fun transform_emptyVariables() {
        val context = PlanningContext<MockTicketPlanResult>()
        val inputString = "test \$var1 \$var2"
        expect(inputString) {
            inputString.transform(context)
        }
    }

    @Test
    fun transform_replacesAllVariables() {
        val context = PlanningContext<MockTicketPlanResult>(
            variables = mapOf(
                "var1" to "value1",
                "var2" to "value2"
            )
        )
        val inputString = "test \$var1 \$var2"
        expect("test value1 value2") {
            inputString.transform(context)
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
            inputString.transform(context)
        }
    }
}