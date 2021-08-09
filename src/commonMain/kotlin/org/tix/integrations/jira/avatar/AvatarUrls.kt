package org.tix.integrations.jira.avatar

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvatarUrls(
    @SerialName("16x16")
    val sixteen: String = "",
    @SerialName("24x24")
    val twentyFour: String = "",
    @SerialName("32x32")
    val thirtyTwo: String = "",
    @SerialName("48x48")
    val fortyEight: String = ""
)
