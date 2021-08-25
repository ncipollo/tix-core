package org.tix.feature.plan.domain.render.jira

import org.tix.feature.plan.domain.render.bodyRenderer
import org.tix.feature.plan.domain.render.common.*
import org.tix.model.ticket.system.TicketSystemType

fun jiraBodyRenderer() = bodyRenderer(TicketSystemType.JIRA) {
    commonRenderers()
    renderer { JiraBulletItemRenderer(it) }
    renderer { JiraBlockQuoteRenderer(it) }
    renderer { JiraCodeBlockRenderer() }
    renderer { JiraCodeSpanRenderer() }
    renderer { JiraEmphasisRenderer() }
    renderer { JiraLinkRenderer() }
    renderer { JiraOrderedItemRenderer(it) }
    renderer { JiraStrongEmphasisRenderer() }
}