package com.agura.task.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agura.task.domain.state.StartCallUiState
import com.agura.task.domain.usecase.StartCallUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartCallViewModel @Inject constructor(
    private val startCallUseCase: StartCallUseCase
) : ViewModel() {

    private val _starNameValidationEffect = MutableSharedFlow<StartCallUiState>()
    val starNameValidationEffect = _starNameValidationEffect.asSharedFlow()

    fun usernameValidation(username: String) = viewModelScope.launch(Dispatchers.Main) {
        _starNameValidationEffect.emit(StartCallUiState.Loading)
        _starNameValidationEffect.emit(startCallUseCase.execute(username))
    }
}