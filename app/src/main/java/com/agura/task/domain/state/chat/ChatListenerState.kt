package com.agura.task.domain.state.chat

import io.agora.chat.ChatMessage


sealed class ChatListenerState {
    data object Idle : ChatListenerState()
    data object Connecting : ChatListenerState()
    data object Connected : ChatListenerState()
    data class Disconnected(val error: Int) : ChatListenerState()
    data class LoggedOut(val errorCode: Int) : ChatListenerState()
    data object TokenExpired : ChatListenerState()
    data object TokenWillExpire : ChatListenerState()
    data class MessageReceived(val messages: List<ChatMessage>) : ChatListenerState()
    data class Failure(val message: String) : ChatListenerState()
}