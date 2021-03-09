package org.tix.model.ticket.body

import kotlinx.serialization.Serializable

@Serializable
sealed class BodySegment

@Serializable
object BlockQuoteSegment : BodySegment()

@Serializable
data class BulletListItemSegment(
    override val body: TicketBody = emptyBody(),
    override val level: Int = 0,
    val marker: String = "-"
) : ListSegment, BodySegment()

@Serializable
data class BulletListSegment(override val body: TicketBody, override val level: Int) : ListSegment, BodySegment()

@Serializable
data class CodeBlockSegment(val code: String = "", val language: String = "") : BodySegment()

@Serializable
data class CodeSpanSegment(val code: String = "") : BodySegment()

@Serializable
data class EmphasisSegment(val text: String = "") : BodySegment()

@Serializable
object LinebreakSegment : BodySegment()

@Serializable
data class LinkSegment(val destination: String = "", val title: String = "") : BodySegment()

@Serializable
data class ParagraphSegment(val body: TicketBody) : BodySegment()

@Serializable
data class OrderedListItemSegment(
    override val body: TicketBody = emptyBody(),
    override val level: Int = 0,
    val number: Int = 0
) : ListSegment, BodySegment()

@Serializable
data class OrderedListSegment(override val body: TicketBody, override val level: Int) : ListSegment, BodySegment()

@Serializable
data class StrongEmphasisSegment(val text: String = "") : BodySegment()

@Serializable
object TextBlockSegment : BodySegment()

@Serializable
data class TextSegment(val text: String = "") : BodySegment()

@Serializable
object ThematicBreakSegment : BodySegment()

@Serializable
data class WhitespaceSegment(val count: Int) : BodySegment()