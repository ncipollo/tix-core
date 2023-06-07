package org.tix.feature.plan.domain.transform

import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.MockTicketPlanResult
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.test.platform.testEnv
import kotlin.test.Test
import kotlin.test.expect

class ActionTransformerTest {
    private  val env = testEnv()

    @Test
    fun transform() {
        val context = PlanningContext<MockTicketPlanResult>(
            variables = mapOf(
                "var1" to "value1"
            )
        )
        val action = Action(arguments = mapOf("key1" to "\$var1"))
        expect(Action(arguments = mapOf("key1" to "value1"))) {
            action.transform(TransformVariableMap(env, context.variables))
        }
    }
}