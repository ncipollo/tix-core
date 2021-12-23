package org.tix.feature.plan.domain.render

import org.tix.ticket.body.BodySegment
import org.tix.ticket.system.TicketSystemType
import kotlin.reflect.KClass

private typealias SegmentRendererFactory<T> = (BodyRenderer) -> BodySegmentRenderer<T>

class BodyRendererBuilder {
    val factories = HashMap<KClass<*>, SegmentRendererFactory<out BodySegment>>()

    inline fun <reified T : BodySegment> renderer(noinline factory: SegmentRendererFactory<T>) {
        factories[T::class] = factory
    }

    fun build(bodyRenderer: BodyRenderer) =
        factories.mapValues { (_, factory) -> factory(bodyRenderer) }
}

fun bodyRenderer(ticketSystemType: TicketSystemType, block: BodyRendererBuilder.() -> Unit) =
    BodyRenderer(
        ticketSystemType,
        BodyRendererBuilder().apply(block)
    )