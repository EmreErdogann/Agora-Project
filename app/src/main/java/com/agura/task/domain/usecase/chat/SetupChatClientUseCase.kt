package com.agura.task.domain.usecase.chat

import android.content.Context
import com.agura.task.core.utils.Constants
import com.agura.task.domain.state.chat.SetupChatState
import io.agora.chat.ChatClient
import io.agora.chat.ChatOptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetupChatClientUseCase @Inject constructor(
    private val applicationContext: Context,
) {

    fun execute(): SetupChatState {

        return try {
            val options = ChatOptions().apply {
                appKey = Constants.APP_CHAT_KEY
            }

            val chatClient = ChatClient.getInstance()
            chatClient.init(applicationContext, options)

            chatClient.setDebugMode(true)

            SetupChatState.Success(chatClient)
        } catch (e: Exception) {
            SetupChatState.Failure(e.message.toString())
        }
    }
}