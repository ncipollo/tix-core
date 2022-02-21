package org.tix.feature.plan.domain.ticket

import kotlin.test.Test
import kotlin.test.expect

class TicketSystemFieldsTest {
    private val expectedFields = setOf("delete_ticket", "update_ticket")

    @Test
    fun fields() {
        val fields = object : TicketSystemFields() {}
        expect("delete_ticket") { fields.deleteTicket }
        expect("update_ticket") { fields.updateTicket }
        expect(expectedFields) { fields.fields }
    }
}