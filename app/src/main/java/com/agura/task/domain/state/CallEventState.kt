package com.agura.task.domain.state

sealed class CallEvent {

    data class UserJoined(val uid: Int, val elapsed: Int) : CallEvent()

    data class JoinChannelSuccess(val channel: String, val uid: Int, val elapsed: Int) : CallEvent()

    data class UserOffline(val uid: Int, val reason: Int) : CallEvent()
}