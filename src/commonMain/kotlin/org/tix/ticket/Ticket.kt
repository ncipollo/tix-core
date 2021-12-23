package org.tix.ticket

import kotlinx.serialization.Serializable
import org.tix.config.data.TicketSystemConfiguration
import org.tix.serialize.dynamic.DynamicElement
import org.tix.serialize.dynamic.emptyDynamic
import org.tix.ticket.body.TicketBody
import org.tix.ticket.body.emptyBody

@Serializable
data class Ticket(
    val title: String = "",
    val body: TicketBody = emptyBody(),
    val fields: DynamicElement = emptyDynamic(),
    val children: List<Ticket> = emptyList()
) {
    val maxDepth by lazy { findMaxDepth(0) }

    fun mergedFields(config: TicketSystemConfiguration?, level: Int): Map<String, Any?> {
        val configFields = config?.fieldsForLevel(level) ?: emptyMap()
        return configFields + fields.asMap()
    }

    private fun findMaxDepth(currentDepth: Int): Int =
        if (children.isEmpty()) {
            currentDepth
        } else {
            children.maxOf { it.findMaxDepth(currentDepth + 1) }
        }
}
