package com.agura.task.domain.state

sealed class GetUsernameState {

    data object Idle : GetUsernameState()

    data object Loading : GetUsernameState()

    data object Success : GetUsernameState()

    data class Failure(val message : String) : GetUsernameState()
}