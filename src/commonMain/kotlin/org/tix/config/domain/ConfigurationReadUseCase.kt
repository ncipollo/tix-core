package org.tix.config.domain

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import org.tix.config.ConfigurationPaths
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.domain.reader.MarkdownConfigurationReader
import org.tix.config.domain.reader.RootConfigurationReader
import org.tix.config.domain.reader.SavedConfigurationReader
import org.tix.config.domain.reader.WorkspaceConfigurationReader
import org.tix.config.reader.RawTixConfigurationReader
import org.tix.domain.FlowTransformer

internal class ConfigurationReadUseCase(
    private val configPaths: ConfigurationPaths,
    private val reader: RawTixConfigurationReader,
    private val markdownConfigurationReader: MarkdownConfigurationReader = MarkdownConfigurationReader(),
    private val rootConfigReader: RootConfigurationReader = RootConfigurationReader(configPaths, reader),
    private val savedConfigReader: SavedConfigurationReader = SavedConfigurationReader(configPaths, reader),
    private val workspaceConfigReader: WorkspaceConfigurationReader = WorkspaceConfigurationReader(configPaths, reader)
) : FlowTransformer<ConfigurationSourceOptions, List<RawTixConfiguration>> {
    override fun transformFlow(upstream: Flow<ConfigurationSourceOptions>): Flow<List<RawTixConfiguration>> =
        upstream
            .transform { configOptions ->
                coroutineScope {
                    val rootDeferred = async { rootConfigReader.readRootConfig(configOptions) }
                    val workspaceDeferred = async { workspaceConfigReader.readWorkspaceConfig(configOptions) }
                    val markdownDeferred = async {
                        markdownConfigurationReader.configFromMarkdown(configOptions.markdownContent)
                    }
                    val workspace = workspaceDeferred.await()
                    val markdownConfig = markdownDeferred.await()
                    val savedDeferred =
                        async { savedConfigReader.readSavedConfigs(configOptions, markdownConfig, workspace) }

                    val configs = listOfNotNull(
                        rootDeferred.await(),
                        *savedDeferred.await().toTypedArray(),
                        workspace,
                        markdownConfig
                    )
                    emit(configs)
                }
            }
}