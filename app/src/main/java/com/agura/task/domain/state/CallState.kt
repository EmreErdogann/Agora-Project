package com.agura.task.domain.state

import io.agora.rtc2.RtcEngine


sealed class CallState {

    data object Idle : CallState()
    data object Loading : CallState()
    data class Success(val rtcEngine: RtcEngine? = null) : CallState()
    data class Failure(val message: String) : CallState()
}

sealed class CallEvent {

    data class UserJoined(val uid: Int, val elapsed: Int) : CallEvent()

    data class JoinChannelSuccess(val channel: String, val uid: Int, val elapsed: Int) : CallEvent()

    data class UserOffline(val uid: Int, val reason: Int) : CallEvent()
}