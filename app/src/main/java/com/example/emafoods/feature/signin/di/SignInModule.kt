package com.example.emafoods.feature.signin.di

import com.example.emafoods.core.domain.network.AuthService
import com.example.emafoods.core.domain.usecase.GetUserDetailsUseCase
import com.example.emafoods.feature.signin.domain.usecase.AddUserDataToRemoteDatabaseUseCase
import com.example.emafoods.feature.signin.domain.usecase.SignInUseCase
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
    fun provideSignInUseCase(
        authService: AuthService
    ) = SignInUseCase(
        authService = authService
    )

    @Provides
    @Singleton
    fun provideAddUserDataToRemoteDatabaseUseCase(
        authService: AuthService,
        getUserDetailsUseCase: GetUserDetailsUseCase
    ) = AddUserDataToRemoteDatabaseUseCase(
        authService = authService,
        getUserDetailsUseCase = getUserDetailsUseCase
    )


}