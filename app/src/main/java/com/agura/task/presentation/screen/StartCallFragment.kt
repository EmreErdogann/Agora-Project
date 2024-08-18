package com.agura.task.presentation.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.agura.task.R
import com.agura.task.core.exts.showToast
import com.agura.task.databinding.FragmentStartCallBinding
import com.agura.task.domain.state.StartCallUiState
import com.agura.task.presentation.viewmodel.StartCallViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StartCallFragment : Fragment(R.layout.fragment_start_call) {

    private val binding: FragmentStartCallBinding by viewBinding(FragmentStartCallBinding::bind)

    private val viewModel: StartCallViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.starNameValidationEffect.collect { state ->
                        binding.isLoading.root.visibility = if (state is StartCallUiState.Loading) View.VISIBLE else View.GONE

                        when (state) {
                            is StartCallUiState.Success -> findNavController().navigate(StartCallFragmentDirections.actionStartCallFragmentToCallFragment(state.username))

                            is StartCallUiState.Failure -> context?.showToast(state.message)

                            else -> {}
                        }
                    }
                }
            }
        }
    }


    private fun setListeners() {
        binding.apply {
            btnStartCall.setOnClickListener {
                viewModel.usernameValidation(txtUsername.text.toString())
            }
        }
    }
}