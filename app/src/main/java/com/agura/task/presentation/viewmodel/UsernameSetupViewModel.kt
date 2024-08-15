package com.agura.task.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agura.task.domain.state.UsernameSetupState
import com.agura.task.domain.usecase.UsernameSetupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UsernameSetupViewModel @Inject constructor(
    private val usernameSetupUseCase: UsernameSetupUseCase
) : ViewModel() {

    private val _uiState by lazy { MutableStateFlow<UsernameSetupState>(UsernameSetupState.Idle) }
    val uiState by lazy { _uiState.asStateFlow() }


    fun saveUsername(username: String) {
        viewModelScope.launch {
            updateUiState { UsernameSetupState.Loading }

            val result = withContext(Dispatchers.IO) {
                usernameSetupUseCase.execute(username)
            }
            updateUiState { result }
        }
    }

    private fun updateUiState(block: UsernameSetupState.() -> UsernameSetupState) {
        _uiState.update { currentState ->
            currentState.block()
        }
    }

}