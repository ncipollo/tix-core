package org.tix.net.http

sealed class AuthMethod {
    object None : AuthMethod()
    data class Basic(val username: String = "", val password: String = "") : AuthMethod()
    data class OAuth2Token(val token: String = "") : AuthMethod()
}
