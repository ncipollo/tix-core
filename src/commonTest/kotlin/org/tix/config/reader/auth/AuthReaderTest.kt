package org.tix.config.reader.auth

import org.tix.config.data.auth.AuthConfiguration
import org.tix.config.data.auth.AuthSource
import org.tix.config.data.raw.RawAuthConfiguration
import org.tix.ticket.system.TicketSystemType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.expect

class AuthReaderTest {
    private companion object {
        val AUTH_CONFIG = AuthConfiguration("user", "mad_secure")
        const val PATH = "path"
        val SYSTEM_TYPE = TicketSystemType.JIRA
    }

    private val envReader = TestAuthReader()
    private val fileReader = TestAuthReader()

    private val authReader = AuthReader(fileReader, envReader)

    @Test
    fun read_whenTypeIsEnv_readsWithEnvReader() {
        val rawConfig = RawAuthConfiguration(source = AuthSource.ENV)
        expect(AUTH_CONFIG) { authReader.read(PATH, rawConfig, SYSTEM_TYPE) }
        envReader.assertCalled(AuthSource.ENV)
    }

    @Test
    fun read_whenTypeIsLocalFile_readsWithFileReader() {
        val rawConfig = RawAuthConfiguration(source = AuthSource.LOCAL_FILE)
        expect(AUTH_CONFIG) { authReader.read(PATH, rawConfig, SYSTEM_TYPE) }
        fileReader.assertCalled(AuthSource.LOCAL_FILE)
    }

    @Test
    fun read_whenTypeIsTixFile_readsWithFileReader() {
        val rawConfig = RawAuthConfiguration(source = AuthSource.TIX_FILE)
        expect(AUTH_CONFIG) { authReader.read(PATH, rawConfig, SYSTEM_TYPE) }
        fileReader.assertCalled(AuthSource.TIX_FILE)
    }

    private class TestAuthReader : AuthSourceReader {
        private var markdownPath: String? = null
        private var rawAuthConfig: RawAuthConfiguration? = null
        private var ticketSystemType: TicketSystemType? = null

        override fun read(
            markdownPath: String,
            rawAuthConfig: RawAuthConfiguration,
            ticketSystemType: TicketSystemType
        ): AuthConfiguration {
            this.markdownPath = markdownPath
            this.rawAuthConfig = rawAuthConfig
            this.ticketSystemType = ticketSystemType

            return AUTH_CONFIG
        }

        fun assertCalled(authSource: AuthSource) {
            assertEquals(PATH, markdownPath)
            assertEquals(RawAuthConfiguration(source = authSource), rawAuthConfig)
            assertEquals(SYSTEM_TYPE, this.ticketSystemType)
        }
    }
}