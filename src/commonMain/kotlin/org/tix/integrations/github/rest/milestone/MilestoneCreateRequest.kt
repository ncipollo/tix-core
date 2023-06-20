package org.tix.integrations.github.rest.milestone

import kotlinx.serialization.Serializable

@Serializable
data class MilestoneCreateRequest(val title: String = "",
                                  val description: String = "")
