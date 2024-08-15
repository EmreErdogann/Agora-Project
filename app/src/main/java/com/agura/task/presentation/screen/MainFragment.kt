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
import com.agura.task.databinding.FragmentMainBinding
import com.agura.task.domain.state.GetUsernameState
import com.agura.task.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    private val viewModel: MainViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        binding.isLoading.root.visibility = if (state is GetUsernameState.Loading) View.VISIBLE else View.GONE

                        when (state) {
                            is GetUsernameState.Failure -> findNavController().navigate(R.id.usernameSetupFragment)

                            is GetUsernameState.Success -> findNavController().navigate(R.id.callFragment)

                            else -> {}
                        }
                    }
                }
            }
        }
    }
}