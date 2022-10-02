package org.tix.ticket

data class RenderedTicket(
    val title: String = "",
    val body: String = "",
    val fields: Map<String, Any?> = emptyMap(),
    val tixId: String = ""
)
