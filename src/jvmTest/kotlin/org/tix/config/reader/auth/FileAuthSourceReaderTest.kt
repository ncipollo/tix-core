package org.tix.config.reader.auth

import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.encodeToString
import org.junit.Test
import org.tix.config.data.auth.AuthConfiguration
import org.tix.config.data.auth.AuthSource
import org.tix.config.data.raw.RawAuthConfiguration
import org.tix.config.reader.ConfigurationFileReader
import org.tix.ticket.system.TicketSystemType
import org.tix.platform.io.FileIO
import org.tix.platform.path.pathByExpandingTilde
import org.tix.serialize.TixSerializers
import java.io.IOException
import kotlin.test.expect

class FileAuthSourceReaderTest {
    private companion object {
        val RAW_CONFIG = RawAuthConfiguration(source = AuthSource.TIX_FILE, file = "auth")
        val JSON_PATH = "~/.tix/auth/auth.json".pathByExpandingTilde().toString()
        val YML_PATH = "~/.tix/auth/auth.yml".pathByExpandingTilde().toString()
    }

    private val config = AuthConfiguration(username = "user")
    private val fileIO = mockk<FileIO<String>>()
    private val jsonString = TixSerializers.json().encodeToString(config)
    private val reader = FileAuthSourceReader(ConfigurationFileReader(fileIO))

    @Test
    fun read() {
        every { fileIO.read(YML_PATH) } throws IOException()
        every { fileIO.read(JSON_PATH) } returns jsonString

        expect(config) { reader.read("", RAW_CONFIG, TicketSystemType.JIRA) }
    }
}