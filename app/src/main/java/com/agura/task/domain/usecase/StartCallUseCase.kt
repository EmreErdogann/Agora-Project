package com.agura.task.domain.usecase

import com.agura.task.core.utils.Constants
import com.agura.task.domain.state.StartCallUiState
import com.agura.task.domain.util.Validator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StartCallUseCase @Inject constructor() {

    fun execute(username: String): StartCallUiState {

        if (username.isEmpty() || username.isBlank())
            return StartCallUiState.Failure(Constants.USERNAME_EMPTY_ERROR)


        if (!Validator.isUserNameLength(username))
            return StartCallUiState.Failure(Constants.USERNAME_LENGTH_ERROR)

        if (!Validator.hasLetterAndNumber(username))
            return StartCallUiState.Failure(Constants.USERNAME_VALIDATION_ERROR)

        return StartCallUiState.Success(username)
    }
}