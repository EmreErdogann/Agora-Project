package com.agura.task.domain.state.chat

import io.agora.chat.ChatClient

sealed class SetupChatState {

    data object Idle : SetupChatState()
    data object Loading : SetupChatState()
    data class Success(val chatClient: ChatClient? = null) : SetupChatState()
    data class Failure(val message: String) : SetupChatState()
}