package org.tix.feature.plan.domain.render.github

import org.tix.feature.plan.domain.render.bodyRenderer
import org.tix.feature.plan.domain.render.common.*
import org.tix.feature.plan.domain.render.jira.JiraStrongEmphasisRenderer
import org.tix.ticket.system.TicketSystemType

fun githubBodyRenderer() = bodyRenderer(TicketSystemType.GITHUB) {
    commonRenderers()
    renderer { GithubBulletItemRenderer(it) }
    renderer { GithubBlockQuoteRenderer(it) }
    renderer { GithubCodeBlockRenderer() }
    renderer { GithubCodeSpanRenderer() }
    renderer { GithubEmphasisRenderer() }
    renderer { GithubLinkRenderer() }
    renderer { GithubOrderedItemRenderer(it) }
    renderer { GithubStrongEmphasisRenderer() }
}