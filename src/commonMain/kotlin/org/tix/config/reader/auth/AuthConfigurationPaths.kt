package org.tix.config.reader.auth

import okio.Path
import org.tix.config.TixConfigurationRoot
import org.tix.config.data.auth.AuthSource
import org.tix.config.data.raw.RawAuthConfiguration
import org.tix.platform.path.pathByExpandingTilde

internal class AuthConfigurationPaths(tixRoot: String = TixConfigurationRoot.STANDARD) {
    private val extensions = listOf("yml", "json")
    private val savedConfigs = "$tixRoot/auth".pathByExpandingTilde()

    fun searchPaths(workspacePath: Path?, config: RawAuthConfiguration) =
        when (source(config)) {
            AuthSource.ENV -> error("AuthSource.ENV should not be using a file reader")
            AuthSource.LOCAL_FILE -> localSearchPaths(workspacePath, config)
            AuthSource.TIX_FILE -> tixSearchPaths(config)
        }

    private fun source(config: RawAuthConfiguration) = config.source ?: AuthSource.LOCAL_FILE

    private fun localSearchPaths(workspacePath: Path?, config: RawAuthConfiguration): List<Path> =
        workspacePath?.let { dir ->
            val fileName = fileName(config)
            if (fileNameHasExtension(fileName)) {
                listOf(dir / fileName)
            } else {
                extensions.map { dir / "${fileName(config)}.$it" }
            }
        } ?: emptyList()

    private fun fileName(config: RawAuthConfiguration) = config.file ?: "tix_auth"

    private fun fileNameHasExtension(fileName: String) = fileName.contains('.')

    private fun tixSearchPaths(config: RawAuthConfiguration): List<Path> {
        val fileName = fileName(config)
        return if (fileNameHasExtension(fileName)) {
            listOf(savedConfigs / fileName)
        } else {
            extensions.map { savedConfigs / "${fileName(config)}.$it" }
        }
    }
}