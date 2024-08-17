package com.agura.task.domain.usecase.chat

import com.agura.task.core.utils.Constants
import com.agura.task.data.repository.datasource.UserLocalDataSource
import com.agura.task.media.RtcTokenBuilder2
import io.agora.CallBack
import io.agora.chat.ChatClient
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ChatLoginUseCase @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource
) {

    suspend fun execute(agoraChatClient: ChatClient) {

        val username = userLocalDataSource.getUsername().firstOrNull() ?: ""

        val tokenBuilder = RtcTokenBuilder2()
        val timestamp = (System.currentTimeMillis() / 1000 * 60).toInt()

        val token = tokenBuilder.buildTokenWithUid(
            Constants.APP_CHAT_ID,
            Constants.APP_CHAT_CERTIFICATE,
            Constants.APP_CHAT_CHANNEL_NAME,
            Constants.APP_CALL_UI,
            RtcTokenBuilder2.Role.ROLE_PUBLISHER, timestamp, timestamp
        )

        agoraChatClient.loginWithAgoraToken(username, token, object : CallBack {
            override fun onSuccess() {

            }

            override fun onError(code: Int, error: String) {

            }
        })
    }
}