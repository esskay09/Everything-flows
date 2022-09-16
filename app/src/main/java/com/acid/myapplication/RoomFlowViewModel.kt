package com.acid.myapplication

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class RoomFlowViewModel(private val app: Application): ViewModel(){


    private val dao = DatabaseProvider.getDb(app.applicationContext).whateverDao()

    init {
        viewModelScope.launch {
            val dummyWhateverList = listOf(
                Whatever(1, "1"),
                Whatever(2, "2"),
                Whatever(3, "3")
            )
            dao.insert(dummyWhateverList)
        }
    }

    fun add() {
        viewModelScope.launch {
            dao.insert(listOf(Whatever(Random(System.currentTimeMillis()).nextInt())))
        }
    }

    val dummyFlow = flowOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .onEach { delay(1000L) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(100L),
            initialValue = 0
        )

    val listRoomFlow = dao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
}

class Factory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomFlowViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoomFlowViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}