package org.tix.feature.plan.domain.parse.state

import org.tix.model.ticket.Ticket
import org.tix.model.ticket.body.BodySegment
import org.tix.model.ticket.body.toTicketBody
import org.tix.serialize.dynamic.DynamicElement
import org.tix.serialize.dynamic.emptyDynamic

internal data class PartialTicket(
    var title: String = "",
    var body: MutableList<BodySegment> = ArrayList(),
    var fields: DynamicElement = emptyDynamic(),
    var children: MutableList<PartialTicket> = ArrayList(),
) : BodyBuilder  {
    private val bodyState: BodyState = BodyState(body)

    override fun addSegments(bodySegments: Array<out BodySegment>) = bodyState.addSegments(bodySegments)

    fun buildNestedBody(buildBlock: () -> Unit) : List<BodySegment> {
        bodyState.pushBody()
        buildBlock()
        return bodyState.popBody()
    }

    fun ticket(): Ticket = Ticket(
        title = title,
        body = body.toTicketBody(),
        fields = fields,
        children = children.map { it.ticket() }
    )
}
