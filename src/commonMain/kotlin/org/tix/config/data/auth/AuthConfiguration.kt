package org.tix.config.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthConfiguration(val username: String = "", val password: String = "")