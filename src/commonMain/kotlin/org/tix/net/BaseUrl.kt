package org.tix.net

class BaseUrl(private val base: String) {
    fun withPath(path: String) = "$base/$path"
}