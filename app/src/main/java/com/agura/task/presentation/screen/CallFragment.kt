package com.agura.task.presentation.screen

import android.os.Bundle
import android.view.SurfaceView
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.agura.task.R
import com.agura.task.core.utils.Constants
import com.agura.task.databinding.FragmentCallBinding
import com.agura.task.domain.state.CallEvent
import com.agura.task.domain.state.CallState
import com.agura.task.presentation.viewmodel.CallViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CallFragment : Fragment(R.layout.fragment_call) {

    private val binding: FragmentCallBinding by viewBinding(FragmentCallBinding::bind)

    private val viewModel: CallViewModel by viewModels()

    private var localSurfaceView: SurfaceView? = null

    private var remoteSurfaceView: SurfaceView? = null

    private var isJoined = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (!viewModel.checkSelfPermission()) {
            viewModel.requestPermissions(requireActivity())
        }

        binding.joinButton.setOnClickListener {
            if (!isJoined) {
                val state = viewModel.startCallUiState.value
                if (state is CallState.Success) {
                    setupLocalVideo(state.mRtcEngine)
                }
            }
            viewModel.joinChannel()
        }

        binding.leaveButton.setOnClickListener {
            if (isJoined) {
                viewModel.leaveChannel()

                if (remoteSurfaceView != null) remoteSurfaceView!!.visibility = View.GONE
                if (localSurfaceView != null) localSurfaceView!!.visibility = View.GONE
                isJoined = false
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.startCallUiState.collect { state ->
                        when (state) {
                            is CallState.Success -> {
                                setupLocalVideo(state.mRtcEngine)
                            }

                            else -> {}
                        }
                    }
                }

                launch {
                    viewModel.rtcEngineEventState.collect {
                        when (it) {
                            is CallEvent.UserJoined -> {
                                val state = viewModel.startCallUiState.value
                                if (state is CallState.Success) {
                                    setupRemoteVideo(it.uid, state.mRtcEngine)
                                }
                            }

                            is CallEvent.UserOffline -> { remoteSurfaceView!!.visibility = View.GONE }

                            is CallEvent.JoinChannelSuccess -> isJoined = true
                        }
                    }
                }
            }
        }

    }


    private fun setupLocalVideo(mRtcEngine: RtcEngine?) {
        localSurfaceView = SurfaceView(requireContext())
        binding.localVideoViewContainer.addView(localSurfaceView)
        mRtcEngine!!.setupLocalVideo(VideoCanvas(localSurfaceView, VideoCanvas.RENDER_MODE_HIDDEN, Constants.APP_UI))
        localSurfaceView!!.visibility = View.VISIBLE

    }


    private fun setupRemoteVideo(uid: Int, mRtcEngine: RtcEngine?) {
        remoteSurfaceView = SurfaceView(requireContext())
        remoteSurfaceView!!.setZOrderMediaOverlay(true)
        binding.remoteVideoViewContainer.addView(remoteSurfaceView)
        mRtcEngine!!.setupRemoteVideo(VideoCanvas(remoteSurfaceView, VideoCanvas.RENDER_MODE_FIT, uid))
        remoteSurfaceView!!.visibility = View.VISIBLE
    }

}