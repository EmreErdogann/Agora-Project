package com.agura.task.domain.usecase.chat

import io.agora.CallBack
import io.agora.chat.ChatClient
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ChatLogoutUseCase @Inject constructor() {


    fun execute(agoraChatClient: ChatClient) {

        agoraChatClient.logout(true, object : CallBack {
            override fun onSuccess() {

            }

            override fun onError(code: Int, error: String) {

            }
        })
    }
}