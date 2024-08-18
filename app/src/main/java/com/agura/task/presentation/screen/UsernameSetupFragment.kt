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
import com.agura.task.databinding.FragmentUsernameSetupBinding
import com.agura.task.domain.state.UsernameSetupState
import com.agura.task.presentation.viewmodel.UsernameSetupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsernameSetupFragment : Fragment(R.layout.fragment_username_setup) {

    private val binding: FragmentUsernameSetupBinding by viewBinding(FragmentUsernameSetupBinding::bind)

    private val viewModel: UsernameSetupViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        binding.isLoading.root.visibility = if (state is UsernameSetupState.Loading) View.VISIBLE else View.GONE

                        when (state) {
                            is UsernameSetupState.Success -> findNavController().navigate(UsernameSetupFragmentDirections.actionUsernameSetupFragmentToStartCallFragment())

                            is UsernameSetupState.Failure -> context?.showToast(state.message)

                            else -> {}
                        }
                    }
                }
            }
        }
    }


    private fun setListeners() {
        binding.apply {
            btnNext.setOnClickListener {
                viewModel.saveUsername(txtUsername.text.toString())
            }
        }
    }
}