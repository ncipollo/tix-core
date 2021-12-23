package org.tix.feature.plan.domain.stats

import kotlin.test.Test
import kotlin.test.expect

class TicketLabelsTest {
    private val labels = ticketLabels(
        TicketLevelLabel("label", "labels"),
        TicketLevelLabel("other", "others")
    )

    @Test
    fun capitalizedLabel() {
        expect("Labels") { labels.capitalizedLabel(0, 2) }
    }

    @Test
    fun label_singular() {
        expect("label") { labels.label(0, 1) }
    }

    @Test
    fun label_plural() {
        expect("labels") { labels.label(0, 2) }
    }

    @Test
    fun label_nextLevel() {
        expect("other") { labels.label(1, 1) }
    }
}