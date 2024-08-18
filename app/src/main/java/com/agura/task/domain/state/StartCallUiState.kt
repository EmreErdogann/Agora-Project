package com.agura.task.domain.state

sealed class StartCallUiState {
    data object Loading : StartCallUiState()
    data class Success(val username : String) : StartCallUiState()
    data class Failure(val message : String) : StartCallUiState()
}