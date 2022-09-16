package com.acid.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val viewModel: RoomFlowViewModel by viewModels {
        Factory(application)
//        ViewModelProvider(this, Factory(application)).get(WhateverViewModel::class.java)
    }
    private val channelsViewModel: ChannelsViewModel by viewModels()

    private val sharedFlowViewModel: SharedFlowViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.add).setOnClickListener {
            viewModel.add()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED)
            {
                launch {
                    sharedFlowViewModel.sharedFlow.collect {
                        log("rcvd -> $it")
                    }
                }
                launch {
                    sharedFlowViewModel.sharedFlow.collect {
                        log("rcvd 2 -> $it")
                    }
                }
            }
        }

        /*lifecycleScope.launchWhenStarted {
            channelsViewModel.channelAsFlow.collect {
                log("rcvd -> $it")
            }
        }*/

/*        lifecycleScope.launch {

            lifecycleScope.launchWhenResumed {
                log("launched")
                viewModel.dummyFlow.collect{
                    log(it.toString())
                }
            }

//            repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                log("launched")
//                viewModel.listRoomFlow.collect {
//                    log("collected $it")
//                }
//            }
        }*/
    }
}


fun log(msg: String) = Log.d("appDebug", msg)