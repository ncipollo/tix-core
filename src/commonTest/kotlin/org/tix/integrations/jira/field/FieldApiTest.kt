package org.tix.integrations.jira.field

import org.tix.fixture.integrations.jiraApi
import org.tix.test.runTestWorkaround
import kotlin.test.Test
import kotlin.test.expect

class FieldApiTest {
    private val api = jiraApi().field

    @Test
    fun fields() = runTestWorkaround {
        val fields = api.fields()
        expect(true) { fields.isNotEmpty() }
    }
}