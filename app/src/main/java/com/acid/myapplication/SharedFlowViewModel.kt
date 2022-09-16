package com.acid.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SharedFlowViewModel : ViewModel() {

    private val _sharedFlow = MutableSharedFlow<Int>(
        replay = 5,
        onBufferOverflow = BufferOverflow.SUSPEND,
    )
    val sharedFlow = _sharedFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            repeat(1000) {
                delay(1000L)
                _sharedFlow.emit(it)
            }
        }
    }
}