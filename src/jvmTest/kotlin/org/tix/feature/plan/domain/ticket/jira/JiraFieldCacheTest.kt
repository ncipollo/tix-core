package org.tix.feature.plan.domain.ticket.jira

import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import org.tix.feature.plan.domain.error.TicketPlanningException
import org.tix.integrations.jira.field.Field
import org.tix.integrations.jira.field.FieldApi
import org.tix.test.runTestWorkaround
import kotlin.test.assertFailsWith
import kotlin.test.expect

class JiraFieldCacheTest {
    private companion object {
        val FIELD1 = Field(id = "1", name = "Field1")
        val FIELD2 = Field(id = "2", name = "field2")
        val FIELDS = listOf(FIELD1, FIELD2)

        val ERROR = RuntimeException("failed")
    }

    private val fieldApi = mockk<FieldApi>()

    @Test
    fun fieldCache_throwsPlanningError() = runTestWorkaround {
        coEvery { fieldApi.fields() } throws ERROR
        assertFailsWith<TicketPlanningException> { fieldApi.fieldCache() }
    }

    @Test
    fun get_lowercaseKey() = runTestWorkaround {
        coEvery { fieldApi.fields() } returns FIELDS
        val cache = fieldApi.fieldCache()

        expect(FIELD1) { cache["field1"] }
        expect(FIELD2) { cache["field2"] }
    }

    @Test
    fun get_uppercaseKey() = runTestWorkaround {
        coEvery { fieldApi.fields() } returns FIELDS
        val cache = fieldApi.fieldCache()

        expect(FIELD1) { cache["Field1"] }
        expect(FIELD2) { cache["Field2"] }
    }

    @Test
    fun get_byID() = runTestWorkaround {
        coEvery { fieldApi.fields() } returns FIELDS
        val cache = fieldApi.fieldCache()

        expect(FIELD1) { cache["1"] }
        expect(FIELD2) { cache["2"] }
    }
}