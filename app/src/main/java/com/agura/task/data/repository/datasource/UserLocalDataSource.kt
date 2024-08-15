package com.agura.task.data.repository.datasource

import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {


    suspend fun saveUsername(username : String): Boolean

    suspend fun getUsername(): Flow<String>

    suspend fun clearData()
}