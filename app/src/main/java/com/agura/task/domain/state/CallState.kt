package com.agura.task.domain.state

import io.agora.rtc2.RtcEngine


sealed class CallState {

    data object Idle : CallState()

    data object Loading : CallState()

    data class Success(val mRtcEngine: RtcEngine? = null) : CallState()

    data class Failure(val message: String) : CallState()
}
