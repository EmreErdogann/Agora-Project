
package com.agura.task.domain.usecase.call

import android.content.Context
import com.agura.task.core.utils.Constants
import com.agura.task.domain.state.CallEvent
import com.agura.task.domain.state.CallState
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StartCallUseCase @Inject constructor(
    private val applicationContext: Context,
    private val coroutineScope: CoroutineScope
) {

    private val _rtcEngineEventState = MutableSharedFlow<CallEvent>()
    val rtcEngineEventState = _rtcEngineEventState.asSharedFlow()

    fun execute(): CallState {

        return try {
            val config = RtcEngineConfig().apply {
                mContext = applicationContext
                mAppId = Constants.APP_ID
                mEventHandler = object : IRtcEngineEventHandler() {
                    override fun onUserJoined(uid: Int, elapsed: Int) {
                        coroutineScope.launch {
                            _rtcEngineEventState.emit(CallEvent.UserJoined(uid, elapsed))
                        }
                    }

                    override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
                        coroutineScope.launch {
                            _rtcEngineEventState.emit(CallEvent.JoinChannelSuccess(channel, uid, elapsed))
                        }
                    }

                    override fun onUserOffline(uid: Int, reason: Int) {
                        coroutineScope.launch {
                            _rtcEngineEventState.emit(CallEvent.UserOffline(uid, reason))
                        }
                    }
                }
            }

            val rtcEngine = RtcEngine.create(config)
            rtcEngine.enableVideo()

            CallState.Success(rtcEngine)
        } catch (e: Exception) {
            CallState.Failure(e.message ?: "Unknown error occurred")
        }
    }
}