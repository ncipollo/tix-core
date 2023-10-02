package org.tix.integrations.jira.field

import org.tix.fixture.integrations.jiraApi
import org.tix.test.runTestWorkaround
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.expect

@Ignore
class FieldApiTest {
    private val api = jiraApi().field

    @Test
    fun fields() = runTestWorkaround {
        val fields = api.fields()
        expect(true) { fields.isNotEmpty() }
    }
}