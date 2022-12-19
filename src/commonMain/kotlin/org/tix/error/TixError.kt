package org.tix.error

import org.tix.domain.FlowResult

data class TixError(
    override val message: String = "something bad happened",
    val mood: String = "😱",
    override val cause: Throwable? = null
) : RuntimeException("$mood: $message", cause)

fun Throwable.toTixError() = TixError(message = this.message ?: "something bad happened", cause = this.cause)

fun FlowResult<*>.toTixError() = exceptionOrNull()?.toTixError() ?: TixError()