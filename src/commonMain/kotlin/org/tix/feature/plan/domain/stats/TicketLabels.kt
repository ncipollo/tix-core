package org.tix.feature.plan.domain.stats

class TicketLabels internal constructor(private val levels: List<TicketLevelLabel>): LevelLabels {
    override fun singularLabel(level: Int) = levels.getOrNull(level)?.singular ?: ""

    override fun pluralLabel(level: Int) = levels.getOrNull(level)?.plural ?: ""

    override val levelCount = levels.size
}

fun ticketLabels(vararg levels: TicketLevelLabel) = TicketLabels(levels.toList())