package com.agura.task.domain.usecase.chat

import com.agura.task.domain.state.chat.SendMessageState
import io.agora.CallBack
import io.agora.chat.ChatClient
import io.agora.chat.ChatMessage
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@Singleton
class SendMessageUseCase @Inject constructor() {

    suspend fun execute(content: String, agoraChatClient: ChatClient): SendMessageState {
        if (content.isEmpty()) return SendMessageState.EmptyContent("Boş değer girmeyiniz.")

        return suspendCoroutine { continuation ->
            val message = ChatMessage.createTextSendMessage(content, "Test")
            message.setMessageStatusCallback(object : CallBack {
                override fun onSuccess() {
                    continuation.resume(SendMessageState.Success(content))
                }

                override fun onError(code: Int, error: String) {
                    continuation.resume(SendMessageState.Failure(code, content))
                }
            })

            agoraChatClient.chatManager().sendMessage(message)
        }
    }
}