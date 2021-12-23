package org.tix.feature.plan.domain.parse.state

import org.tix.ticket.body.BodySegment

interface BodyBuilder {
    fun addSegments(bodySegments: Array<out BodySegment>)
}