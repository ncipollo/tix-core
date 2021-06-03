package org.tix.config.domain

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import okio.Path
import org.tix.config.ConfigurationPaths
import org.tix.config.data.TixConfiguration
import org.tix.domain.FlowTransformer
import org.tix.platform.path.pathByExpandingTilde

internal class ConfigurationReadUseCase(
    private val reader: ConfigurationReader
) : FlowTransformer<String, List<TixConfiguration>> {
    override fun transformFlow(upstream: Flow<String>): Flow<List<TixConfiguration>> =
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

    private fun readRootConfig() = ConfigurationPaths.RootConfig.searchPaths.firstConfig()

    private fun readWorkspaceConfig(markdownPath: Path) =
        ConfigurationPaths.workspaceSearchPaths(markdownPath).firstConfig()

    private fun readSavedConfig(workspaceConfig: TixConfiguration?) =
        ConfigurationPaths.savedConfigSearchPaths(workspaceConfig).firstConfig()

    private fun List<Path>?.firstConfig() = this?.firstNotNullOfOrNull { reader.readConfig(it) }
}