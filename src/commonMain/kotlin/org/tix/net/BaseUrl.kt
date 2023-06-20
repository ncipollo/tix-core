package org.tix.net

data class BaseUrl(private val base: String) {
    fun withPath(path: String) = "$base/$path"

    override fun toString() = base
}