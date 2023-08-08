package org.tix.feature.plan.presentation

import app.cash.turbine.test
import io.ktor.utils.io.core.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okio.Path.Companion.toPath
import okio.fakefilesystem.FakeFileSystem
import org.tix.config.data.TixConfiguration
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.domain.*
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.domain.transform
import org.tix.error.toTixError
import org.tix.feature.plan.domain.combiner.MarkdownPlanAction
import org.tix.feature.plan.domain.parse.MarkdownFileSource
import org.tix.feature.plan.domain.parse.MarkdownSourceValidator
import org.tix.feature.plan.domain.parse.MarkdownTextSource
import org.tix.fixture.config.rawTixConfiguration
import org.tix.fixture.config.ticketSystemAuth
import org.tix.fixture.config.tixConfiguration
import org.tix.test.testErrorTransformer
import org.tix.test.testTransformer
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PlanSourceCombinerTest {
    private companion object {
        val CONFIG_LIST = listOf(rawTixConfiguration)
        val ERROR = RuntimeException("oh noes")
        val TIX_ERROR = ERROR.toTixError()
        const val MARKDOWN = "markdown"
        const val PATH = "a/path.md"

        val BAKER_ACTION = ConfigBakerAction(rawTixConfiguration, ticketSystemAuth)
        val CONFIG_OPTIONS = ConfigurationSourceOptions.forMarkdownSource(PATH)
        val AUTH_ACTION = AuthConfigAction(CONFIG_OPTIONS.workspaceDirectory, rawTixConfiguration)
    }

    private val configSuccessfulReadSource = testTransformer(CONFIG_OPTIONS to CONFIG_LIST)
    private val source = flowOf(MarkdownPlanAction(PATH, false))
    private val fileSystem = FakeFileSystem()
    private val markdownValidator = MarkdownSourceValidator(fileSystem)

    @BeforeTest
    fun before() {
        val path = PATH.toPath()
        fileSystem.createDirectories(path.parent!!)
        fileSystem.write(path) {
            write("test".toByteArray())
        }
    }

    @Test
    fun transformFlow_whenAllSourceSucceed_emitsSuccess() = runTest {
        val combiner = combiner()
        val expectedResult = PlanSourceResult.Success(tixConfiguration, MARKDOWN)
        source.transform(combiner)
            .test {
                assertEquals(expectedResult, awaitItem())
                awaitComplete()
            }
    }

    @Test
    fun transformFlow_withTextMarkdownSource_emitsSuccess() = runTest {
        val combiner = combiner(
            authReadSource = testTransformer(AUTH_ACTION.copy(path = null) to ticketSystemAuth),
            configReadUseCase = testTransformer(CONFIG_OPTIONS.copy(workspaceDirectory = null) to CONFIG_LIST)
        )
        val expectedResult = PlanSourceResult.Success(tixConfiguration, MARKDOWN)
        val action = MarkdownPlanAction(MarkdownTextSource(MARKDOWN))

        flowOf(action)
            .transform(combiner)
            .test {
                assertEquals(expectedResult, awaitItem())
                awaitComplete()
            }
    }

    @Test
    fun transformFlow_authConfigReadFails_emitsError() = runTest {
        val combiner = combiner(authReadSource = authConfigError())
        source.transform(combiner)
            .test {
                assertEquals(TIX_ERROR.message, (awaitItem() as PlanSourceResult.Error).error.message)
                awaitComplete()
            }
    }

    @Test
    fun transformFlow_configBakerFails_emitsError() = runTest {
        val combiner = combiner(configBakerUseCase = configBakerError())
        val expectedResult = PlanSourceResult.Error(TIX_ERROR)
        source.transform(combiner)
            .test {
                assertEquals(expectedResult, awaitItem())
                awaitComplete()
            }
    }

    @Test
    fun transformFlow_configMergeSourceFails_emitsError() = runTest {
        val combiner = combiner(configMergeUseCase = configMergeErrorUseCase())
        val expectedResult = PlanSourceResult.Error(TIX_ERROR)
        source.transform(combiner)
            .test {
                assertEquals(expectedResult, awaitItem())
                awaitComplete()
            }
    }

    @Test
    fun transformFlow_configReadFails_emitsError() = runTest {
        val combiner = combiner(configReadUseCase = configReadError())
        source.transform(combiner)
            .test {
                assertEquals(TIX_ERROR.message, (awaitItem() as PlanSourceResult.Error).error.message)
                awaitComplete()
            }
    }

    @Test
    fun transformFlow_markdownSourceFails_emitsError() = runTest {
        val combiner = combiner(markdownFileUseCase = markdownErrorFileUseCase())
        val expectedResult = PlanSourceResult.Error(TIX_ERROR)
        source.transform(combiner)
            .test {
                assertEquals(expectedResult, awaitItem())
                awaitComplete()
            }
    }

    @Test
    fun transformFlow_markdownValidationFails() = runTest {
        fileSystem.delete(PATH.toPath())
        val expectedError = runCatching { markdownValidator.validate(MarkdownFileSource(PATH)) }
            .exceptionOrNull()
            ?.toTixError()
        val expectedResult = PlanSourceResult.Error(expectedError!!)
        source.transform(combiner())
            .test {
                assertEquals(expectedResult, awaitItem())
                awaitComplete()
            }
    }


    private fun combiner(
        authReadSource: FlowTransformer<AuthConfigAction, TicketSystemAuth> = authConfigUseCase(),
        configReadUseCase: FlowTransformer<ConfigurationSourceOptions, List<RawTixConfiguration>> = configSuccessfulReadSource,
        configBakerUseCase: FlowTransformer<ConfigBakerAction, FlowResult<TixConfiguration>> = configBakerSuccess(),
        configMergeUseCase: FlowTransformer<List<RawTixConfiguration>, FlowResult<RawTixConfiguration>> = configMergeSuccessUseCase(),
        markdownFileUseCase: FlowTransformer<String, FlowResult<String>> = markdownSuccessFileUseCase()
    ) = planSourceCombiner(
        configUseCase = configurationUseCase(
            authReadSource,
            configBakerUseCase,
            configReadUseCase,
            configMergeUseCase,
        ),
        markdownFileUseCase = markdownFileUseCase,
        markdownValidator = markdownValidator
    )

    private fun authConfigUseCase() = testTransformer(AUTH_ACTION to ticketSystemAuth)

    private fun authConfigError() = testErrorTransformer<AuthConfigAction, TicketSystemAuth>(ERROR)

    private fun configReadError() = testErrorTransformer<ConfigurationSourceOptions, List<RawTixConfiguration>>(ERROR)

    private fun configBakerError() =
        testTransformer(BAKER_ACTION to FlowResult.failure<TixConfiguration>(ERROR))

    private fun configBakerSuccess() = testTransformer(BAKER_ACTION to FlowResult.success(tixConfiguration))

    private fun configMergeErrorUseCase() =
        testTransformer(CONFIG_LIST to FlowResult.failure<RawTixConfiguration>(ERROR))

    private fun configMergeSuccessUseCase() = testTransformer(CONFIG_LIST to FlowResult.success(rawTixConfiguration))

    private fun markdownErrorFileUseCase() = testTransformer(PATH to FlowResult.failure<String>(ERROR))

    private fun markdownSuccessFileUseCase() = testTransformer(PATH to FlowResult.success(MARKDOWN))
}