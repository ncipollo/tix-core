package org.tix.ticket

import org.tix.fixture.config.tixConfiguration
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.Test
import kotlin.test.expect

class TicketTest {
    @Test
    fun mergedFields_whenConfigIsNull_returnsTicketFields() {
        val ticket = Ticket(fields = DynamicElement(mapOf("ticket_field" to "value")))
        val expected = ticket.fields.asMap()
        expect(expected) { ticket.mergedFields(null, 0) }
    }

    @Test
    fun mergedFields_whenTicketFieldsAreNotAMap_returnsMergedFields() {
        val ticket = Ticket(fields = DynamicElement("fails"))
        val expected = tixConfiguration.jira!!.fieldsForLevel(0)
        expect(expected) { ticket.mergedFields(tixConfiguration.jira, 0) }
    }

    @Test
    fun mergedFields_whenTicketHasNoFields_returnsConfigFields() {
        val ticket = Ticket()
        val expected = tixConfiguration.jira!!.fieldsForLevel(0)
        expect(expected) { ticket.mergedFields(tixConfiguration.jira, 0) }
    }

    @Test
    fun mergedFields_whenTicketHasValidFields_returnsMergedFields() {
        val ticket = Ticket(fields = DynamicElement(mapOf("ticket_field" to "value")))
        val expected = tixConfiguration.jira!!.fieldsForLevel(0) + ticket.fields.asMap()
        expect(expected) { ticket.mergedFields(tixConfiguration.jira, 0) }
    }

    @Test
    fun mergedFields_whenTicketHasValidFields_andNonZeroLevel_returnsMergedFields() {
        val ticket = Ticket(fields = DynamicElement(mapOf("ticket_field" to "value")))
        val expected = tixConfiguration.jira!!.fieldsForLevel(1) + ticket.fields.asMap()
        expect(expected) { ticket.mergedFields(tixConfiguration.jira, 1) }
    }

    @Test
    fun maxDepth_withChildren_deepChild() {
        val childOfTheDeep = Ticket("aldrich")
        val grandChild =  Ticket("grandchild", children = listOf(childOfTheDeep))
        val child1 = Ticket("child1", children = listOf(grandChild))
        val child2 = Ticket("child2", children = listOf(grandChild))
        val root = Ticket("root", children = listOf(child1, child2))

        expect(3) { root.maxDepth }
    }

    @Test
    fun maxDepth_withChildren_equallyDeepChildren() {
        val grandChild1 =  Ticket("grandchild1")
        val grandChild2 =  Ticket("grandchild1")
        val child1 = Ticket("child1", children = listOf(grandChild1))
        val child2 = Ticket("child2", children = listOf(grandChild2))
        val root = Ticket("root", children = listOf(child1, child2))

        expect(2) { root.maxDepth }
    }

    @Test
    fun maxDepth_withoutChildren() {
        val root = Ticket("root")
        expect(0) { root.maxDepth }
    }
}