package org.tix.platform

import org.tix.test.platform.testEnv
import kotlin.test.Test
import kotlin.test.expect

class SanitizedEnvTest {
    private val wrappedEnv = testEnv(
        "JIRA_USERNAME" to "jira_name",
        "GITHUB_API_TOKEN" to "github_token"
    )

    @Test
    fun get() {
        val env = sanitizedEnv(wrappedEnv)
        expect("") { env["JIRA_USERNAME"] }
        expect("") { env["GITHUB_API_TOKEN"] }
    }
}