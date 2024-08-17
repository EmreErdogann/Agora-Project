package com.agura.task.presentation.screen

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.agura.task.R
import com.agura.task.core.utils.Constants
import com.agura.task.databinding.FragmentStartCallBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartCallFragment : Fragment(R.layout.fragment_start_call) {

    private val binding: FragmentStartCallBinding by viewBinding(FragmentStartCallBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }


    private fun setListeners() {
        binding.apply {
            btnStartCall.setOnClickListener {
                val username = txtUsername.text.toString()
                findNavController().navigate(R.id.callFragment, bundleOf(Constants.CALL_PAGE_ARG to username))
            }
        }
    }
}