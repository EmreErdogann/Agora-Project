package com.agura.task.domain.usecase.chat

import io.agora.CallBack
import io.agora.chat.ChatClient
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ChatLoginUseCase @Inject constructor() {

    fun execute(agoraChatClient: ChatClient) {

        agoraChatClient.loginWithAgoraToken("as12-312ad-bbdfa8", "007eJxTYDgvrOBgxVIS8+rpu1Ptx4/YNM05yGv7YqrdpF87nHxj044oMBgYWJiam6ckJxqbmJtYJBkkpRqZpRkaGackGaQZmSdbVArvT2sIZGTwWuPLysjAysAIhCC+CkOKgblRorm5ga5pkmWqrqFhapquRbKlsa6ZpbmRhaVBmqWhWRIAiWsm8Q==", object : CallBack {
            override fun onSuccess() {
               println("Başarılı")
            }

            override fun onError(code: Int, error: String) {
                if (code == 200) {
                    println("onError")

                }
            }
        })
    }
}