package org.tix.serialize

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.tix.fixture.config.rawTixConfiguration
import kotlin.test.Test
import kotlin.test.expect

class TixSerializersTest {
    private val json = TixSerializers.json()
    private val yaml = TixSerializers.yaml()

    @Test
    fun json() {
        val text = json.encodeToString(rawTixConfiguration)
        expect(rawTixConfiguration) { json.decodeFromString(text) }
    }

    @Test
    fun yaml() {
        val text = yaml.encodeToString(rawTixConfiguration)
        expect(rawTixConfiguration) { yaml.decodeFromString(text) }
    }
}