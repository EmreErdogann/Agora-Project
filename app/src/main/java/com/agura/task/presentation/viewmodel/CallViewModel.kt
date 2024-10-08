package com.agura.task.presentation.viewmodel

import android.app.Activity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agura.task.core.utils.Constants
import com.agura.task.domain.state.CallEvent
import com.agura.task.domain.state.CallState
import com.agura.task.domain.state.chat.ChatListenerState
import com.agura.task.domain.state.chat.SendMessageState
import com.agura.task.domain.state.chat.SetupChatState
import com.agura.task.domain.usecase.PermissionUseCase
import com.agura.task.domain.usecase.call.ClearRtcEngineUseCase
import com.agura.task.domain.usecase.call.JoinChannelUseCase
import com.agura.task.domain.usecase.call.LeaveChannelUseCase
import com.agura.task.domain.usecase.call.CallUseCase
import com.agura.task.domain.usecase.chat.ChatListenerUseCase
import com.agura.task.domain.usecase.chat.ChatLoginUseCase
import com.agura.task.domain.usecase.chat.SendMessageUseCase
import com.agura.task.domain.usecase.chat.SetupChatClientUseCase
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
    private val startCallUseCase: CallUseCase,
    private val joinChannelUseCase: JoinChannelUseCase,
    private val leaveChannelUseCase: LeaveChannelUseCase,
    private val clearRtcEngineUseCase: ClearRtcEngineUseCase,
    private val permissionUseCase: PermissionUseCase,
    private val setupChatClientUseCase: SetupChatClientUseCase,
    private val chatListenerUseCase: ChatListenerUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val chatLoginUseCase: ChatLoginUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _startCallUiState by lazy { MutableStateFlow<CallState>(CallState.Idle) }
    val startCallUiState by lazy { _startCallUiState.asStateFlow() }


    private val _rtcEngineEventState = MutableSharedFlow<CallEvent>()
    val rtcEngineEventState by lazy { _rtcEngineEventState.asSharedFlow() }

    private val _setupChatUiState by lazy { MutableStateFlow<SetupChatState>(SetupChatState.Idle) }
    val setupChatUiState by lazy { _setupChatUiState.asStateFlow() }

    private val _chatListenerUiState by lazy { MutableStateFlow<ChatListenerState>(ChatListenerState.Idle) }
    val chatListenerUiState by lazy { _chatListenerUiState.asStateFlow() }

    private val _chatSendMessageUiState = MutableSharedFlow<SendMessageState>()
    val chatSendMessageUiState by lazy { _chatSendMessageUiState.asSharedFlow() }

    init {
        start()

        joinChannel()

        callEventHandler()

        startChat()

        chatListener()
    }


    private fun start() {
        viewModelScope.launch {
            _startCallUiState.emit(startCallUseCase.execute())
        }
    }


    private fun joinChannel() {
        (_startCallUiState.value as CallState.Success).rtcEngine?.let {
            joinChannelUseCase.execute(it)
        }
    }

    private fun callEventHandler() = viewModelScope.launch(Dispatchers.Main) {
        startCallUseCase.rtcEngineEventState.collect {
            _rtcEngineEventState.emit(it)
        }
    }


    fun leaveChannel() {
        (_startCallUiState.value as CallState.Success).rtcEngine?.let {
            leaveChannelUseCase.execute(it)
        }
    }


    fun checkSelfPermission(): Boolean {
        return permissionUseCase.checkSelfPermission()
    }

    fun requestPermissions(activity: Activity) {
        permissionUseCase.requestPermissions(activity)
    }


    private fun startChat() = viewModelScope.launch(Dispatchers.Main) {
        _setupChatUiState.emit(SetupChatState.Loading)
        _setupChatUiState.emit(setupChatClientUseCase.execute())
    }


    private fun chatListener() = viewModelScope.launch(Dispatchers.Main) {
        (_setupChatUiState.value as SetupChatState.Success).chatClient?.let {
            _chatListenerUiState.emit(chatListenerUseCase.execute(it))
        }
    }

    fun sendMessage(content: String) = viewModelScope.launch(Dispatchers.Main) {
        (_setupChatUiState.value as SetupChatState.Success).chatClient?.let {
            val sendToUsername = savedStateHandle.get<String>(Constants.CALL_PAGE_ARG) ?: ""
            _chatSendMessageUiState.emit(sendMessageUseCase.execute(content, it, sendToUsername))
        }
    }

    fun chatLogin() = viewModelScope.launch(Dispatchers.Main) {
        (_setupChatUiState.value as SetupChatState.Success).chatClient?.let {
            chatLoginUseCase.execute(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        leaveChannel()
        (_startCallUiState.value as CallState.Success).rtcEngine?.let {
            clearRtcEngineUseCase.execute(it)
        }
        setRtcEngineNull()
    }


    private fun setRtcEngineNull() {
        val currentState = _startCallUiState.value
        if (currentState is CallState.Success) {
            _startCallUiState.value = currentState.copy(rtcEngine = null)
        }
    }
}