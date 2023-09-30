package org.tix.config.reader

import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.encodeToString
import okio.Path.Companion.toPath
import org.junit.Test
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.platform.io.FileIO
import org.tix.serialize.TixSerializers
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.assertEquals
import kotlin.test.expect

class RawTixConfigurationReaderTest {
    private companion object {
        val JSON_PATH = "path/tix.json".toPath()
    }

    private val fileIO = mockk<FileIO<String>>()
    private val fileReader = ConfigurationFileReader(fileIO)
    private val reader = RawTixConfigurationReader(fileReader)

    @Test
    fun firstConfigFile() {
        val config = RawTixConfiguration(include = DynamicElement("included"))
        val jsonString = TixSerializers.json().encodeToString(config)
        every { fileIO.read(JSON_PATH.toString()) } returns jsonString
        expect(config) { reader.firstConfigFile(listOf(JSON_PATH)) }
    }

    @Test
    fun serialize_matrix() {
        val config = RawTixConfiguration(
            matrix = mapOf(
                "matrix_1" to listOf(DynamicElement("value_1"), DynamicElement("value_2")),
                "matrix_2" to listOf(DynamicElement("value_3"), DynamicElement("value_4")),
            )
        )

        val jsonString = TixSerializers.json().encodeToString(config);

        val expectedString = """
            {
                "matrix": {
                    "matrix_1": [
                        "value_1",
                        "value_2"
                    ],
                    "matrix_2": [
                        "value_3",
                        "value_4"
                    ]
                }
            }
        """.trimIndent()
        assertEquals(expectedString, jsonString)
        expect(config) { TixSerializers.json().decodeFromString(jsonString) }
    }
}