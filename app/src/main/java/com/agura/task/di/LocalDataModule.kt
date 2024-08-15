package com.agura.task.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.agura.task.data.repository.datasource.UserLocalDataSource
import com.agura.task.data.repository.datasource.implementation.UserLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {

    private val Context.userLocalDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_local")


    @Provides
    @Singleton
    fun provideUsernameLocalDataSource(@Named("user_local") dataStore: DataStore<Preferences>): UserLocalDataSource {
        return UserLocalDataSourceImpl(dataStore)
    }

    @Provides
    @Singleton
    @Named("user_local")
    fun provideUsernameDataStoreInstance(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.userLocalDataStore
    }
}