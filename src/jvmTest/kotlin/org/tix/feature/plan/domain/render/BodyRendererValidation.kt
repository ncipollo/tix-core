package org.tix.feature.plan.domain.render

import org.tix.model.ticket.body.BodySegment
import kotlin.test.expect

fun BodyRenderer.supportsAllRenderers() {
    expect(true) {
        val segmentClasses = BodySegment::class.sealedSubclasses
        val missing = segmentClasses.filter { !supportedRenderers.contains(it) }
        if (missing.isEmpty()) {
            true
        } else {
            println("Missing: ${missing.map { it.simpleName }}")
            false
        }
    }
}