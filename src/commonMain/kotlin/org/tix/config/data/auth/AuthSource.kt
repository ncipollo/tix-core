package org.tix.config.data.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AuthSource {
    @SerialName("env")
    ENV,
    @SerialName("local_file")
    LOCAL_FILE,
    @SerialName("tix_file")
    TIX_FILE
}