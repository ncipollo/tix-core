package org.tix.feature.plan.domain.render.jira

import org.tix.ticket.body.BodySegment
import kotlin.test.Test
import kotlin.test.expect

class JiraBodyRendererTest {
    private val renderer = jiraBodyRenderer()

    @Test
    fun supportsAllSegments() {
        expect(true) {
            val segmentClasses = BodySegment::class.sealedSubclasses
            val missing = segmentClasses.filter { !renderer.supportedRenderers.contains(it) }
            if (missing.isEmpty()) {
                true
            } else {
                println("Missing: ${missing.map { it.simpleName }}")
                false
            }
        }
    }
}