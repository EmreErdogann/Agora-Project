package com.agura.task.domain.usecase.chat

import io.agora.CallBack
import io.agora.chat.ChatClient
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ChatLoginUseCase @Inject constructor() {

    fun execute(agoraChatClient: ChatClient) {

        val token = "007eJxTYFARCTwkz3Sd49TBLJbS4gfbPxgLGp/irDda+fn1rlNqJZwKDAYGFqbm5inJicYm5iYWSQZJqUZmaYZGxilJBmlG5skW0csPpDUEMjLkHHrLxMjAysAIhCC+CkOKgblRorm5ga5pkmWqrqFhapquRbKlsa6ZpbmRhaVBmqWhWRIA5rQmNg=="
        agoraChatClient.loginWithAgoraToken("as12-312ad-bbdfa8", token, object : CallBack {
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