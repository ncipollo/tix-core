package org.tix.config.reader.auth

import org.tix.config.data.auth.AuthConfiguration
import org.tix.config.data.raw.RawAuthConfiguration
import org.tix.ticket.system.TicketSystemType
import org.tix.test.platform.testEnv
import kotlin.test.Test
import kotlin.test.expect

class EnvAuthSourceReaderTest {
    private companion object {
        val AUTH_CONFIG = RawAuthConfiguration()
        const val PATH = "path"
        val SYSTEM_TYPE = TicketSystemType.JIRA
    }

    @Test
    fun read_whenEnvHasAPIKey_returnsAuthWithApiKey() {
        val env = testEnv(
            "JIRA_USERNAME" to "user",
            "JIRA_API_TOKEN" to "token",
            "JIRA_PASSWORD" to "password",
        )
        val reader = EnvAuthSourceReader(env)
        expect(AuthConfiguration("user", "token")) { reader.read(PATH, AUTH_CONFIG, SYSTEM_TYPE) }
    }

    @Test
    fun read_whenEnvHasOnlyPassword_returnsAuthWithApiKey() {
        val env = testEnv(
            "JIRA_USERNAME" to "user",
            "JIRA_PASSWORD" to "password",
        )
        val reader = EnvAuthSourceReader(env)
        expect(AuthConfiguration("user", "password")) { reader.read(PATH, AUTH_CONFIG, SYSTEM_TYPE) }
    }

    @Test
    fun read_whenEnvIsMissingAuth_returnsEmptyAuth() {
        val env = testEnv()
        val reader = EnvAuthSourceReader(env)
        expect(AuthConfiguration()) { reader.read(PATH, AUTH_CONFIG, SYSTEM_TYPE) }
    }
}