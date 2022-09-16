package com.acid.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ChannelsViewModel : ViewModel() {

    private val channel = Channel<Int>(
        capacity = Channel.BUFFERED,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
        onUndeliveredElement = {
            log("undelivered -> $it")
        }
    )
    val channelAsFlow = channel.receiveAsFlow()

    init {
        viewModelScope.launch {
            repeat(1000) {
                delay(1000)
                channel.send(it)
            }
        }
    }

}