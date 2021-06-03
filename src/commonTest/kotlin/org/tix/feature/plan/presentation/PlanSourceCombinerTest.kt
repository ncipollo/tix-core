package org.tix.feature.plan.presentation

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import org.tix.config.data.TixConfiguration
import org.tix.config.data.dynamic.DynamicProperty
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.domain.transform
import org.tix.error.TixError
import org.tix.test.runBlockingTest
import org.tix.test.testTransformer
import kotlin.test.Test
import kotlin.test.assertEquals

class PlanSourceCombinerTest {
    private companion object {
        val CONFIG = TixConfiguration(include = DynamicProperty(string = "tix"))
        val CONFIG_LIST = listOf(CONFIG)
        val ERROR = TixError()
        const val MARKDOWN = "markdown"
        const val PATH = "a/path"
    }

    private val configReadSource = testTransformer(PATH to CONFIG_LIST)
    private val source = flowOf(PATH)

    @Test
    fun transformFlow_whenAllSourceSucceed_emitsSuccess() = runBlockingTest {
        val combiner = combiner()
        val expectedResult = PlanSourceResult.Success(CONFIG, MARKDOWN)
        source.transform(combiner)
            .test {
                assertEquals(expectedResult, expectItem())
                expectComplete()
            }
    }

    @Test
    fun transformFlow_configMergeSourceFails_emitsError() = runBlockingTest {
        val combiner = combiner(configMergeSource = configMergeErrorSource())
        val expectedResult = PlanSourceResult.Error(ERROR)
        source.transform(combiner)
            .test {
                assertEquals(expectedResult, expectItem())
                expectComplete()
            }
    }

    @Test
    fun transformFlow_markdownSourceFails_emitsError() = runBlockingTest {
        val combiner = combiner(markdownSource = markdownErrorSource())
        val expectedResult = PlanSourceResult.Error(ERROR)
        source.transform(combiner)
            .test {
                assertEquals(expectedResult, expectItem())
                expectComplete()
            }
    }

    private fun combiner(
        configMergeSource: FlowTransformer<List<TixConfiguration>, FlowResult<TixConfiguration>> = configMergeSuccessSource(),
        markdownSource: FlowTransformer<String, FlowResult<String>> = markdownSuccessSource()
    ) = planSourceCombiner(configReadSource, configMergeSource, markdownSource)

    private fun configMergeErrorSource() = testTransformer(CONFIG_LIST to FlowResult.failure<TixConfiguration>(ERROR))

    private fun configMergeSuccessSource() = testTransformer(CONFIG_LIST to FlowResult.success(CONFIG))

    private fun markdownErrorSource() = testTransformer(PATH to FlowResult.failure<String>(ERROR))

    private fun markdownSuccessSource() = testTransformer(PATH to FlowResult.success(MARKDOWN))
}