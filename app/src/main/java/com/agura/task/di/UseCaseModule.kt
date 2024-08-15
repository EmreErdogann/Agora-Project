package com.agura.task.di

import android.content.Context
import com.agura.task.domain.usecase.PermissionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun providePermissionUseCase(@ApplicationContext context: Context): PermissionUseCase {
        return PermissionUseCase(context)
    }

}