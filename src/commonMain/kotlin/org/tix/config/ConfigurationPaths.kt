package org.tix.config

import okio.Path
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.platform.path.pathByExpandingTilde

internal class ConfigurationPaths(private val tixRoot: String = TixConfigurationRoot.STANDARD) {
    companion object {
        val shared = ConfigurationPaths()

        val rootConfigSearchPaths = shared.rootConfigSearchPaths
        fun workspaceIncludedConfigSearchPaths(config: RawTixConfiguration?) =
            shared.workspaceIncludedConfigSearchPaths(config)

        fun workspaceSearchPaths(workspaceDirectory: Path) = shared.workspaceSearchPaths(workspaceDirectory)
    }

    private val extensions = listOf("yml", "json")
    private val savedConfigs = "$tixRoot/configs".pathByExpandingTilde()

    val rootConfigSearchPaths get() = extensions.map { "$tixRoot/config.$it".pathByExpandingTilde() }

    fun workspaceIncludedConfigSearchPaths(config: RawTixConfiguration?) =
        config?.include
            ?.asList()
            ?.filterIsInstance<String>()
            ?.flatMap { name -> savedConfigSearchPaths(name) }
            ?: emptyList()

    fun savedConfigSearchPaths(savedConfigName: String) =
        extensions.map { savedConfigs / "$savedConfigName.$it" }

    fun workspaceSearchPaths(workspaceDirectory: Path) =
        extensions.map { workspaceDirectory / "tix.$it" }
}