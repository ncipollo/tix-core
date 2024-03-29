package org.tix.integrations.jira.transition

import org.tix.fixture.integrations.jiraApi
import org.tix.test.runTestWorkaround
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.expect

@Ignore
class TransitionApiTest {
    private val api = jiraApi().transition

    @Test
    fun transitions() = runTestWorkaround {
        val transitions = api.transitions("TIX-978")
        expect(setOf("To Do", "In Progress", "Done")) {
            transitions.map { it.name }.toSet()
        }
    }
}