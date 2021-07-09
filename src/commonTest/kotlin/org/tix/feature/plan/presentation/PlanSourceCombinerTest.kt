package org.tix.feature.plan.presentation

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import org.tix.config.data.TixConfiguration
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.domain.AuthConfigAction
import org.tix.config.domain.ConfigBakerAction
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.domain.transform
import org.tix.error.TixError
import org.tix.fixture.config.rawTixConfiguration
import org.tix.fixture.config.ticketSystemAuth
import org.tix.fixture.config.tixConfiguration
import org.tix.test.runBlockingTest
import org.tix.test.testTransformer
import kotlin.test.Test
import kotlin.test.assertEquals

class PlanSourceCombinerTest {
    private companion object {
        val CONFIG_LIST = listOf(rawTixConfiguration)
        val ERROR = TixError()
        const val MARKDOWN = "markdown"
        const val PATH = "a/path"

        val AUTH_ACTION = AuthConfigAction(PATH, rawTixConfiguration)
        val BAKER_ACTION = ConfigBakerAction(rawTixConfiguration, ticketSystemAuth)
    }

    private val configReadSource = testTransformer(PATH to CONFIG_LIST)
    private val source = flowOf(PATH)

    @Test
    fun transformFlow_whenAllSourceSucceed_emitsSuccess() = runBlockingTest {
        val combiner = combiner()
        val expectedResult = PlanSourceResult.Success(tixConfiguration, MARKDOWN)
        source.transform(combiner)
            .test {
                assertEquals(expectedResult, expectItem())
                expectComplete()
            }
    }

    @Test
    fun transformFlow_configBackerFails_emitsError() = runBlockingTest {
        val combiner = combiner(configBakerSource = configBackerError())
        val expectedResult = PlanSourceResult.Error(ERROR)
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
        configBakerSource: FlowTransformer<ConfigBakerAction, FlowResult<TixConfiguration>> = configBackerSuccess(),
        configMergeSource: FlowTransformer<List<RawTixConfiguration>, FlowResult<RawTixConfiguration>> = configMergeSuccessSource(),
        markdownSource: FlowTransformer<String, FlowResult<String>> = markdownSuccessSource()
    ) = planSourceCombiner(
        authConfigUseCase(),
        configBakerSource,
        configReadSource,
        configMergeSource,
        markdownSource
    )

    private fun authConfigUseCase() = testTransformer(AUTH_ACTION to ticketSystemAuth)

    private fun configBackerError() =
        testTransformer(BAKER_ACTION to FlowResult.failure<TixConfiguration>(ERROR))

    private fun configBackerSuccess() = testTransformer(BAKER_ACTION to FlowResult.success(tixConfiguration))

    private fun configMergeErrorSource() =
        testTransformer(CONFIG_LIST to FlowResult.failure<RawTixConfiguration>(ERROR))

    private fun configMergeSuccessSource() = testTransformer(CONFIG_LIST to FlowResult.success(rawTixConfiguration))

    private fun markdownErrorSource() = testTransformer(PATH to FlowResult.failure<String>(ERROR))

    private fun markdownSuccessSource() = testTransformer(PATH to FlowResult.success(MARKDOWN))
}