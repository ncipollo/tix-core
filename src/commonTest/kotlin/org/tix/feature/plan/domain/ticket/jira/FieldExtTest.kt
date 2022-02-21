package org.tix.feature.plan.domain.ticket.jira

import org.tix.integrations.jira.field.Field
import org.tix.integrations.jira.field.FieldSchema
import kotlin.test.Test
import kotlin.test.expect

class FieldExtTest {
    @Test
    fun useValueKey_schemaTypeIsNoOption() {
        val field = Field(schema = FieldSchema(type = "i_dunno_something_else"))
        expect(false) { field.useValueKey() }
    }

    @Test
    fun useValueKey_schemaTypeIsOption() {
        val field = Field(schema = FieldSchema(type = "option"))
        expect(true) { field.useValueKey() }
    }
}