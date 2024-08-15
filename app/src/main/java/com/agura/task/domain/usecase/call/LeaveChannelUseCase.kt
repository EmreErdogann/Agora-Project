package com.agura.task.domain.usecase.call

import io.agora.rtc2.RtcEngine
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LeaveChannelUseCase @Inject constructor() {

    fun execute(mRtcEngine: RtcEngine? = null) {
        mRtcEngine?.leaveChannel()
    }
}