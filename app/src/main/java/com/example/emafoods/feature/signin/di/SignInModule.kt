package com.example.emafoods.feature.signin.di

import com.example.emafoods.core.domain.network.AuthService
import com.example.emafoods.feature.signin.domain.usecase.AddUserDataToRemoteDatabaseUseCase
import com.example.emafoods.feature.signin.domain.usecase.SignInAnonymousUseCase
import com.example.emafoods.feature.signin.domain.usecase.SignInGoogleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SignInModule {

    @Provides
    @Singleton
    fun provideSignInGoogleUseCase(
        authService: AuthService
    ) = SignInGoogleUseCase(
        authService = authService
    )

    @Provides
    @Singleton
    fun provideSignInAnonymousUseCase(
        authService: AuthService
    ) = SignInAnonymousUseCase(
        authService = authService
    )

    @Provides
    @Singleton
    fun provideAddUserDataToRemoteDatabaseUseCase(
        authService: AuthService,
    ) = AddUserDataToRemoteDatabaseUseCase(
        authService = authService,
    )
}