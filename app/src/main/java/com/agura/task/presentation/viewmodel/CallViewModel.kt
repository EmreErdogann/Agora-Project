package com.agura.task.presentation.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agura.task.domain.state.CallEvent
import com.agura.task.domain.state.CallState
import com.agura.task.domain.usecase.PermissionUseCase
import com.agura.task.domain.usecase.call.ClearRtcEngineUseCase
import com.agura.task.domain.usecase.call.JoinChannelUseCase
import com.agura.task.domain.usecase.call.LeaveChannelUseCase
import com.agura.task.domain.usecase.call.StartCallUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallViewModel @Inject constructor(
    private val startCallUseCase: StartCallUseCase,
    private val joinChannelUseCase: JoinChannelUseCase,
    private val leaveChannelUseCase: LeaveChannelUseCase,
    private val clearRtcEngineUseCase : ClearRtcEngineUseCase,
    private val permissionUseCase: PermissionUseCase
) : ViewModel() {

    private val _startCallUiState by lazy { MutableStateFlow<CallState>(CallState.Idle) }
    val startCallUiState by lazy { _startCallUiState.asStateFlow() }


    private val _rtcEngineEventState = MutableSharedFlow<CallEvent>()
    val rtcEngineEventState = _rtcEngineEventState.asSharedFlow()

    init {
        start()

        callEventHandler()
    }

    private fun start() {
        viewModelScope.launch {
            _startCallUiState.emit(startCallUseCase.execute())
        }
    }


    fun joinChannel() {
        (_startCallUiState.value as CallState.Success).mRtcEngine?.let {
            joinChannelUseCase.execute(it)
        }
    }

    private fun callEventHandler() = viewModelScope.launch(Dispatchers.Main) {
        startCallUseCase.rtcEngineEventState.collect {
            _rtcEngineEventState.emit(it)
        }
    }


    fun leaveChannel() {
        (_startCallUiState.value as CallState.Success).mRtcEngine?.let {
            leaveChannelUseCase.execute(it)
        }
    }


    fun checkSelfPermission(): Boolean {
        return permissionUseCase.checkSelfPermission()
    }

    fun requestPermissions(activity: Activity) {
        permissionUseCase.requestPermissions(activity)
    }

    override fun onCleared() {
        super.onCleared()
        (_startCallUiState.value as CallState.Success).mRtcEngine?.let {
            clearRtcEngineUseCase.execute(it)
        }
        setRtcEngineNull()
    }


    private fun setRtcEngineNull() {
        val currentState = _startCallUiState.value
        if (currentState is CallState.Success) {
            _startCallUiState.value = currentState.copy(mRtcEngine = null)
        }
    }
}