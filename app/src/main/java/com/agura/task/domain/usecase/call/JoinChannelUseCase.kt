package com.agura.task.domain.usecase.call

import com.agura.task.media.RtcTokenBuilder2
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.RtcEngine
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class JoinChannelUseCase @Inject constructor() {


    fun execute(mRtcEngine: RtcEngine) {

        val tokenBuilder = RtcTokenBuilder2()
        val timestamp = (System.currentTimeMillis() / 1000 * 60).toInt()

        val token = tokenBuilder.buildTokenWithUid(
            com.agura.task.core.utils.Constants.APP_CALL_ID,
            com.agura.task.core.utils.Constants.APP_CALL_CERTIFICATE,
            com.agura.task.core.utils.Constants.APP_CALL_CHANNAL_NAME,
            com.agura.task.core.utils.Constants.APP_CALL_UI,
            RtcTokenBuilder2.Role.ROLE_PUBLISHER, timestamp, timestamp
        )

        val options = ChannelMediaOptions()
        options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER

        mRtcEngine.startPreview()
        mRtcEngine.joinChannel(token, com.agura.task.core.utils.Constants.APP_CALL_CHANNAL_NAME, com.agura.task.core.utils.Constants.APP_CALL_UI, options)

    }
}