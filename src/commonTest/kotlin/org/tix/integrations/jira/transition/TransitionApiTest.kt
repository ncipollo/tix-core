package org.tix.integrations.jira.transition

import org.tix.fixture.integrations.jiraApi
import org.tix.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.expect

class TransitionApiTest {
    private val api = jiraApi().transition

    @Test
    fun transitions() = runBlockingTest {
        val transitions = api.transitions("TIX-1")
        expect(setOf("To Do", "In Progress", "Done")) {
            transitions.map { it.name }.toSet()
        }
    }
}