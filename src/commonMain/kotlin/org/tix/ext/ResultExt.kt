package org.tix.ext

/**
 * Checks if the result has an error and if it is of an expected type. Throws the error if it exists and is an
 * unexpected type.
 */
inline fun <reified E : Throwable> Result<*>.checkExpectedError() {
    val error = exceptionOrNull()
    error?.let {
        if (error::class != E::class) throw error
    }
}