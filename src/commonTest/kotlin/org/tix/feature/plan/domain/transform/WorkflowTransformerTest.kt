package org.tix.feature.plan.domain.transform

import org.tix.config.data.Action
import org.tix.config.data.Workflow
import org.tix.feature.plan.domain.ticket.MockTicketPlanResult
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.test.platform.testEnv
import kotlin.test.Test
import kotlin.test.expect

class WorkflowTransformerTest {
    private  val env = testEnv()

    @Test
    fun transform() {
        val context = PlanningContext<MockTicketPlanResult>(
            variables = mapOf(
                "var1" to "value1"
            )
        )
        val action1 = Action(arguments = mapOf("key1" to "\$var1"))
        val action2 = Action(arguments = mapOf("key2" to "\$var1"))
        val workflow = Workflow(actions = listOf(action1, action2))

        val expectedWorkflow = Workflow(
            actions = listOf(
                Action(arguments = mapOf("key1" to "value1")),
                Action(arguments = mapOf("key2" to "value1"))
            )
        )
        expect(expectedWorkflow) {
            workflow.transform(TransformVariableMap(env, context.variables))
        }
    }
}