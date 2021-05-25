package org.tix.feature.plan

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.tix.domain.FlowTransformer
import org.tix.domain.transform
import org.tix.model.ticket.Ticket

class TixPlanSubmit internal constructor(
    private val markdownSource: FlowTransformer<String, Result<String>>,
    private val parserUseCase: FlowTransformer<String, Result<List<Ticket>>>
) {
    private val markdownPaths = BroadcastChannel<String>(Channel.BUFFERED)

    // TODO: Add plan events channel, make that base
    fun submit(markdownPath: String) =
        flowOf(markdownPath)
            .transform(markdownSource)
            .filter { it.isSuccess }
            .map { it.getOrThrow() }
            .transform(parserUseCase)
}