package com.agura.task.domain.usecase

import com.agura.task.core.utils.Constants
import com.agura.task.data.repository.datasource.UserLocalDataSource
import com.agura.task.domain.state.UsernameSetupState
import com.agura.task.domain.util.Validator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsernameSetupUseCase @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource
) {

    suspend fun execute(username: String): UsernameSetupState {

        if (username.isEmpty() || username.isBlank())
            return UsernameSetupState.Failure(Constants.USERNAME_EMPTY_ERROR)

        if (!Validator.isUserNameLength(username))
            return UsernameSetupState.Failure(Constants.USERNAME_LENGTH_ERROR)

        if (!Validator.hasLetterAndNumber(username))
            return UsernameSetupState.Failure(Constants.USERNAME_VALIDATION_ERROR)

        val result = userLocalDataSource.saveUsername(username)
        return if (result)
            UsernameSetupState.Success
        else
            UsernameSetupState.Failure(Constants.USERNAME_SAVE_ERROR)
    }
}