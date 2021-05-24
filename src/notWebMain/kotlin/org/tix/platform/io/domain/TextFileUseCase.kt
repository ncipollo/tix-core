package org.tix.platform.io.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import okio.IOException
import org.tix.domain.FlowTransformer
import org.tix.ext.checkExpectedError
import org.tix.platform.io.FileIO

class TextFileUseCase(private val fileIO: FileIO<String>) : FlowTransformer<String, Result<String>> {
    override fun transformFlow(upstream: Flow<String>): Flow<Result<String>> = upstream.transform {
        val result = kotlin.runCatching { fileIO.read(it) }
        result.checkExpectedError<IOException>()
        emit(result)
    }
}