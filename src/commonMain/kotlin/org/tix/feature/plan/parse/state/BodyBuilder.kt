package org.tix.feature.plan.parse.state

import org.tix.model.ticket.body.BodySegment

interface BodyBuilder {
    fun addSegments(bodySegments: Array<out BodySegment>)
}