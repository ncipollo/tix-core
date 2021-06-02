package org.tix.presentation

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel

abstract class TixViewModel : ViewModel() {
    private val channels: MutableList<SendChannel<*>> = mutableListOf()

    override fun onCleared() {
        channels.forEach { it.close() }
    }

    fun <T> eventChannel() = BroadcastChannel<T>(Channel.BUFFERED).also { channels += it }
}