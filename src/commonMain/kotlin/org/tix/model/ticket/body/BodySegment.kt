package org.tix.model.ticket.body

import kotlinx.serialization.Serializable

@Serializable
sealed class BodySegment

@Serializable
object BlockQuoteSegment : BodySegment()

@Serializable
data class BulletListEndSegment(val level: Int) : BodySegment()

@Serializable
data class BulletListItemSegment(val level: Int = 0, val marker: String = "-") : BodySegment()

@Serializable
data class BulletListStartSegment(val marker: String = "") : BodySegment()

@Serializable
data class CodeBlockSegment(val code: String = "", val language: String = "") : BodySegment()

@Serializable
data class CodeSpanSegment(val code: String = "") : BodySegment()

@Serializable
data class EmphasisSegment(val text: String = "") : BodySegment()

@Serializable
object LineBreakSegment : BodySegment()

@Serializable
data class LinkSegment(val text: String = "", val url: String = "") : BodySegment()

@Serializable
data class ParagraphSegment(val body: TicketBody): BodySegment()

@Serializable
data class OrderedEndSegment(val level: Int) : BodySegment()

@Serializable
data class OrderedListItemSegment(val level: Int = 0, val number: Int = 0) : BodySegment()

@Serializable
data class OrderedStartSegment(val level: Int, val startingNumber: Int = 0) : BodySegment()

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