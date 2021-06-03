package org.tix.error

import org.tix.domain.FlowResult

data class TixError(
    override val message: String = "something bad happened",
    val mood: String = "😱"
) : RuntimeException("$mood: $message")

fun Throwable.toTixError() = TixError(message = this.message ?: "something bad happened")

fun FlowResult<*>.toTixError() = exceptionOrNull()?.toTixError()!!