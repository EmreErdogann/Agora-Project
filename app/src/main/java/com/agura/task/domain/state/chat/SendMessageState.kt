package com.agura.task.domain.state.chat


sealed class SendMessageState {
    data object Idle : SendMessageState()
    data object Loading : SendMessageState()
    data class EmptyContent(val message : String) : SendMessageState()
    data class Success(val content: String) : SendMessageState()
    data class Failure(val code: Int, val error: String) : SendMessageState()


}