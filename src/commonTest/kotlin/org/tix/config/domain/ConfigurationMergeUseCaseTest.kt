package org.tix.config.domain

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.merge.flatten
import org.tix.domain.transform
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigurationMergeUseCaseTest {
    private val configs = listOf(
        RawTixConfiguration(include = DynamicElement("original1"), variables = mapOf("original1" to "var1")),
        RawTixConfiguration(include = DynamicElement("original2"), variables = mapOf("original2" to "var1"))
    )
    private val overrideConfigs = listOf(
        RawTixConfiguration(include = DynamicElement("override1"), variables = mapOf("override1" to "var3")),
        RawTixConfiguration(include = DynamicElement("override2"), variables = mapOf("override2" to "var4"))
    )

    @Test
    fun transformFlow_whenConfigurationsAreProvided_emittedMergedResult() = runTest {
        val useCase = ConfigurationMergeUseCase(overrideConfigs)
        val expected = (configs + overrideConfigs).flatten()
        flowOf(configs).transform(useCase)
            .test {
                assertEquals(expected, awaitItem().getOrThrow())
                awaitComplete()
            }
    }

    @Test
    fun transformFlow_whenNoConfigurations_emitsErrorResult() = runTest {
        val useCase = ConfigurationMergeUseCase()
        flowOf(emptyList<RawTixConfiguration>()).transform(useCase)
            .test {
                assertEquals(TixConfigurationMergeException, awaitItem().exceptionOrNull())
                awaitComplete()
            }
    }
}