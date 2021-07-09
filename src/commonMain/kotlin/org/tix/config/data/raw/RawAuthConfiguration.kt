package org.tix.config.data.raw

import kotlinx.serialization.Serializable
import org.tix.config.data.auth.AuthSource

@Serializable
data class RawAuthConfiguration(val source: AuthSource? = null, val file: String? = null)
