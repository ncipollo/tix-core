package org.tix.platform.io.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import okio.IOException
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.domain.checkExpectedError
import org.tix.ext.toFlowResult
import org.tix.platform.io.FileIO

class TextFileUseCase(private val fileIO: FileIO<String>) : FlowTransformer<String, FlowResult<String>> {
    override fun transformFlow(upstream: Flow<String>): Flow<FlowResult<String>> = upstream.transform {
        val result = kotlin.runCatching { fileIO.read(it) }.toFlowResult()
        result.checkExpectedError<IOException>()
        emit(result)
    }
}