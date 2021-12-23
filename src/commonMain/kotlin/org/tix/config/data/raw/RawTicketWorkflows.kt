package org.tix.config.data.raw

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RawTicketWorkflows(
    @SerialName("after_all") val afterAll: List<RawWorkflow> = emptyList(),
    @SerialName("after_each") val afterEach: List<RawWorkflow> = emptyList(),
    @SerialName("before_all") val beforeAll: List<RawWorkflow> = emptyList(),
    @SerialName("before_each") val beforeEach: List<RawWorkflow> = emptyList()
)
