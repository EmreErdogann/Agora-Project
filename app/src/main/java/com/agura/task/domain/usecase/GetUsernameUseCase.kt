package com.agura.task.domain.usecase

import com.agura.task.core.utils.Constants.USERNAME_LOCAL_EMPTY
import com.agura.task.data.repository.datasource.UserLocalDataSource
import com.agura.task.domain.state.GetUsernameState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUsernameUseCase @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource
) {
    suspend fun execute(): GetUsernameState {
        delay(1000L)

        val result = userLocalDataSource.getUsername().firstOrNull() ?: ""
        if (result.isEmpty() || result.isBlank())
            return GetUsernameState.Failure(USERNAME_LOCAL_EMPTY)

        return GetUsernameState.Success
    }
}