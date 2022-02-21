package org.tix.feature.plan.domain.ticket

abstract class TicketSystemFields {
    private val mutableFields = mutableSetOf<String>()
    protected fun field(fieldKey: String) = fieldKey.also { mutableFields += it }
    val fields: Set<String> get() = mutableFields.toSet()

    val deleteTicket = field("delete_ticket")
    val updateTicket = field("update_ticket")
}

object GenericTicketFields: TicketSystemFields()