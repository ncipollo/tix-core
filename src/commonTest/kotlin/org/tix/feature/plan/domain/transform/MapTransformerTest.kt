package org.tix.feature.plan.domain.transform

import org.tix.feature.plan.domain.ticket.MockTicketPlanResult
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.mockPlanningContext
import org.tix.test.platform.testEnv
import kotlin.test.Test
import kotlin.test.expect

class MapTransformerTest {
    private  val env = testEnv()

    @Test
    fun transform_empty() {
        expect(emptyMap()) {
            emptyMap<String, Any?>().transform(TransformVariableMap(env, mockPlanningContext().variables))
        }
    }

    @Test
    fun transform_mapsValues() {
        val inputMap = mapOf(
            "integer" to 42,
            "string1" to "\$var1",
            "string2" to "\$var2",
            "\$var3" to "value3"
        )
        val context = PlanningContext<MockTicketPlanResult>(
            variables = mapOf(
                "var1" to "value1",
                "var2" to "value2",
                "var3" to "string3"
            )
        )
        val expectedMap = mapOf(
            "integer" to 42,
            "string1" to "value1",
            "string2" to "value2",
            "string3" to "value3"
        )
        expect(expectedMap) {
            inputMap.transform(TransformVariableMap(env, context.variables))
        }
    }
}