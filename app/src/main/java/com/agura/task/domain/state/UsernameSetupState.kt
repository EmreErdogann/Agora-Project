package com.agura.task.domain.state

sealed class UsernameSetupState {


    data object Idle : UsernameSetupState()

    data object Loading : UsernameSetupState()

    data object Success : UsernameSetupState()

    data class Failure(val message : String) : UsernameSetupState()
}