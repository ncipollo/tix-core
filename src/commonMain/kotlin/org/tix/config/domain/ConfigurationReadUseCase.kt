package org.tix.config.domain

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import okio.Path
import org.tix.config.ConfigurationPaths
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.reader.RawTixConfigurationReader
import org.tix.domain.FlowTransformer
import org.tix.platform.path.pathByExpandingTilde

internal class ConfigurationReadUseCase(
    private val reader: RawTixConfigurationReader
) : FlowTransformer<String, List<RawTixConfiguration>> {
    override fun transformFlow(upstream: Flow<String>): Flow<List<RawTixConfiguration>> =
        upstream
            .map { it.pathByExpandingTilde() }
            .transform { markdownPath ->
                coroutineScope {
                    val rootDeferred = async { readRootConfig() }
                    val workspaceDeferred = async { readWorkspaceConfig(markdownPath) }
                    val workspace = workspaceDeferred.await()
                    val savedDeferred = async { readSavedConfig(workspace) }

                    val configs = listOfNotNull(rootDeferred.await(), savedDeferred.await(), workspace)
                    emit(configs)
                }
            }

    private fun readRootConfig() = reader.firstConfigFile(ConfigurationPaths.RootConfig.searchPaths)

    private fun readWorkspaceConfig(markdownPath: Path) =
        ConfigurationPaths.workspaceSearchPaths(markdownPath)?.let {
            reader.firstConfigFile(it)
        }

    private fun readSavedConfig(workspaceConfig: RawTixConfiguration?) =
        ConfigurationPaths.savedConfigSearchPaths(workspaceConfig)?.let {
            reader.firstConfigFile(it)
        }
}