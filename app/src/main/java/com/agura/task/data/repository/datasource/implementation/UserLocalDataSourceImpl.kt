package com.agura.task.data.repository.datasource.implementation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.agura.task.data.repository.datasource.UserLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>
) : UserLocalDataSource {

    private val usernamePrefKey = stringPreferencesKey(KEY_USERNAME)

    companion object {
        private const val KEY_USERNAME = "user_name"
    }

    override suspend fun saveUsername(username: String): Boolean {
        return try {
            dataStore.edit {
                it[usernamePrefKey] = username
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getUsername(): Flow<String> {
        return dataStore.data.map {
            return@map it[usernamePrefKey] ?: ""
        }
    }

    override suspend fun clearData() {
        dataStore.edit {
            it.clear()
        }
    }

}