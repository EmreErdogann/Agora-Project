package com.agura.task.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agura.task.domain.state.GetUsernameState
import com.agura.task.domain.usecase.GetUsernameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUsernameUseCase: GetUsernameUseCase
) : ViewModel() {

    private val _uiState by lazy { MutableStateFlow<GetUsernameState>(GetUsernameState.Loading) }
    val uiState by lazy { _uiState.asStateFlow() }

    init {
        getUsername()
    }


    private fun getUsername() {
        viewModelScope.launch {

            val result = withContext(Dispatchers.IO) {
                getUsernameUseCase.execute()
            }

            updateUiState { result }
        }
    }

    private fun updateUiState(block: GetUsernameState.() -> GetUsernameState) {
        _uiState.update { currentState ->
            currentState.block()
        }
    }
}