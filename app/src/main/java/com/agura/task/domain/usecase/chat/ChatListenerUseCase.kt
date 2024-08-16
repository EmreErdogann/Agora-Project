package com.agura.task.domain.usecase.chat

import com.agura.task.domain.state.chat.ChatListenerState
import io.agora.ConnectionListener
import io.agora.chat.ChatClient
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@Singleton
class ChatListenerUseCase @Inject constructor() {

    suspend fun execute(chatClient: ChatClient): ChatListenerState {
        return suspendCoroutine { continuation ->
            chatClient.chatManager().addMessageListener { messages ->
                continuation.resume(ChatListenerState.MessageReceived(messages))
            }

            chatClient.addConnectionListener(object : ConnectionListener {
                override fun onConnected() {
                    continuation.resume(ChatListenerState.Connected)
                }

                override fun onDisconnected(error: Int) {
                    continuation.resume(ChatListenerState.Disconnected(error))
                }

                override fun onLogout(errorCode: Int) {
                    continuation.resume(ChatListenerState.LoggedOut(errorCode))
                }

                override fun onTokenExpired() {
                    continuation.resume(ChatListenerState.TokenExpired)
                }

                override fun onTokenWillExpire() {
                    continuation.resume(ChatListenerState.TokenWillExpire)
                }
            })
        }
    }
}