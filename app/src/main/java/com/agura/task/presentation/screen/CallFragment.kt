package com.agura.task.presentation.screen

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.SurfaceView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.agura.task.R
import com.agura.task.core.exts.showToast
import com.agura.task.databinding.FragmentCallBinding
import com.agura.task.domain.state.CallEvent
import com.agura.task.domain.state.CallState
import com.agura.task.domain.state.chat.SendMessageState
import com.agura.task.domain.state.chat.SetupChatState
import com.agura.task.presentation.viewmodel.CallViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CallFragment : Fragment(R.layout.fragment_call) {

    private val binding: FragmentCallBinding by viewBinding(FragmentCallBinding::bind)

    private val viewModel: CallViewModel by viewModels()

    private var remoteSurfaceView: SurfaceView? = null

    private var isJoined = false

    private var isChatJoined = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewModel.checkSelfPermission()) {
            viewModel.requestPermissions(requireActivity())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.rtcEngineEventState.collect {
                        when (it) {
                            is CallEvent.UserJoined -> {
                                val state = viewModel.startCallUiState.value
                                if (state is CallState.Success) {
                                    setupRemoteVideo(it.uid, state.rtcEngine)
                                }
                            }

                            is CallEvent.UserOffline -> { remoteSurfaceView!!.visibility = View.GONE }

                            is CallEvent.JoinChannelSuccess -> isJoined = true
                        }
                    }
                }

                launch {
                    viewModel.setupChatUiState.collect {
                        when(it) {
                            is SetupChatState.Success -> {
                                viewModel.chatLogin()
                            }

                            else -> {}
                        }
                    }
                }

                launch {
                    viewModel.chatSendMessageUiState.collect {
                        when(it) {
                            is SendMessageState.EmptyContent -> {
                                context?.showToast(it.message)
                            }

                            is SendMessageState.Success -> {
                                binding.contentEditText.setText("")
                                displayMessage(it.content,true)
                            }
                            else -> {}
                        }
                    }
                }
            }
        }

        setListeners()


    }

    private fun setListeners() {
        binding.apply {
            sendButton.setOnClickListener {
                val message = contentEditText.text.toString()
                viewModel.sendMessage(message)
            }
        }
    }


    private fun setupRemoteVideo(uid: Int, mRtcEngine: RtcEngine?) {
        remoteSurfaceView = SurfaceView(requireContext())
        remoteSurfaceView!!.setZOrderMediaOverlay(true)
        binding.remoteVideoViewContainer.addView(remoteSurfaceView)
        mRtcEngine!!.setupRemoteVideo(VideoCanvas(remoteSurfaceView, VideoCanvas.RENDER_MODE_FIT, uid))
        remoteSurfaceView!!.visibility = View.VISIBLE
    }


    private fun displayMessage(messageText: String?, isSentMessage: Boolean) {
        val messageTextView = TextView(context)
        messageTextView.text = messageText
        messageTextView.setPadding(10, 10, 10, 10)

        val messageList = binding.messageList
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT
        )
        if (isSentMessage) {
            params.gravity = Gravity.END
            messageTextView.setBackgroundColor(Color.parseColor("#DCF8C6"))
            params.setMargins(100, 25, 15, 5)
        } else {
            messageTextView.setBackgroundColor(Color.parseColor("white"))
            params.setMargins(15, 25, 100, 5)
        }

        messageList.addView(messageTextView, params)
    }

}