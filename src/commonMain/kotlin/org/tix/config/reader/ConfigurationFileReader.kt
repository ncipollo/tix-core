package org.tix.config.reader

import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import okio.Path
import org.tix.platform.io.FileIO
import org.tix.serialize.TixSerializers

internal class ConfigurationFileReader(private val fileIO: FileIO<String>) {
    val json = TixSerializers.json()
    val yaml = TixSerializers.yaml()

    inline fun <reified T> firstConfigFile(paths: List<Path>?) =
        paths?.firstNotNullOfOrNull { readConfig<T>(it) }

    inline fun <reified T> readConfig(path: Path): T? =
        if (path.name.endsWith(".json")) {
            runCatching { readJsonConfig<T>(path) }
                .throwSerializationErrors()
                .getOrNull()
        } else {
            runCatching { readYamlConfig<T>(path) }
                .throwSerializationErrors()
                .getOrNull()
        }

    private fun <T> Result<T>.throwSerializationErrors() = recover {
        if (it is SerializationException) {
            throw it
        }
        null
    }

    inline fun <reified T> readJsonConfig(path: Path): T = json.decodeFromString(read(path))

    inline fun <reified T> readYamlConfig(path: Path): T = yaml.decodeFromString(read(path))

    fun read(path: Path) = fileIO.read(path.toString())
}