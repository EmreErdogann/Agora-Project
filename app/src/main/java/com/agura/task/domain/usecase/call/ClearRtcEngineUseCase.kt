package com.agura.task.domain.usecase.call

import io.agora.rtc2.RtcEngine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClearRtcEngineUseCase @Inject constructor() {


    fun execute(mRtcEngine: RtcEngine? = null) {
        mRtcEngine!!.stopPreview()
        mRtcEngine.leaveChannel()
    }
}