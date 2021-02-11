package org.tix.model.ticket.body

sealed class BodySegment

object BlockQuoteSegment : BodySegment()

data class BulletListEndSegment(val level: Int) : BodySegment()

data class BulletListItemSegment(val level: Int = 0, val marker: String = "-") : BodySegment()

data class BulletListStartSegment(val marker: String = "") : BodySegment()

data class CodeBlockSegment(val code: String = "", val language: String = "") : BodySegment()

data class CodeSpanSegment(val code: String = "") : BodySegment()

data class EmphasisSegment(val text: String = "") : BodySegment()

data class LinkSegment(val text: String = "", val url: String = "") : BodySegment()

data class OrderedEndSegment(val level: Int) : BodySegment()

data class OrderedListItemSegment(val level: Int = 0, val number: Int = 0) : BodySegment()

data class OrderedStartSegment(val level: Int, val startingNumber: Int = 0) : BodySegment()

data class StrongEmphasisSegment(val text: String = "") : BodySegment()

object TextBlockSegment : BodySegment()

data class TextSegment(val text: String = "") : BodySegment()

object ThematicBreakSegment : BodySegment()