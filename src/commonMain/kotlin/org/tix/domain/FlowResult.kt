package org.tix.domain

/**
 * Similar to a result but represented by runtime objects (instead of inline value classes). This works around an issue
 * which causes class cast exceptions when using Results across coroutines.
 */
sealed class FlowResult<T>(open val value: T?, open val throwable: Throwable?) {
    companion object {
        fun <T> failure(throwable: Throwable?): FlowResult<T> = Failure(throwable)
        fun <T> success(value: T?): FlowResult<T> = Success(value)
    }

    data class Failure<T>(override val throwable: Throwable?) : FlowResult<T>(null, throwable)
    data class Success<T>(override val value: T?) : FlowResult<T>(value, null)

    fun exceptionOrNull() = throwable

    fun getOrNull(): T? = value

    fun getOrThrow(): T = value ?: throw throwable!!

    val isFailure get() = this is Failure

    val isSuccess get() = this is Success

    inline fun <R> map(transform: (value: T) -> R): FlowResult<R> =
        when(this) {
            is Failure -> failure(throwable)
            is Success -> try {
                success(transform(getOrThrow()))
            } catch (ex: Throwable) {
                failure(ex)
            }
        }
}

/**
 * Checks if the result has an error and if it is of an expected type. Throws the error if it exists and is an
 * unexpected type.
 */
inline fun <reified E : Throwable> FlowResult<*>.checkExpectedError() {
    val error = exceptionOrNull()
    error?.let {
        if (error::class != E::class) throw error
    }
}