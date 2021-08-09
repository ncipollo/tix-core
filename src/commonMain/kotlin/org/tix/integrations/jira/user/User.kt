package org.tix.integrations.jira.user

import kotlinx.serialization.Serializable
import org.tix.integrations.jira.avatar.AvatarUrls

@Serializable
data class User(
    val self: String = "",
    val accountId: String = "",
    val accountType: String = "",
    val name: String = "",
    val key: String = "",
    val emailAddress: String = "",
    val avatarUrls: AvatarUrls = AvatarUrls(),
    val displayName: String = "",
    val active: Boolean = false,
    val timeZone: String = "",
    val locale: String = "",
    val applicationKeys: List<String> = emptyList(),
)
